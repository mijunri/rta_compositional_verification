package learning.ttt;

import learning.Membership;
import rta.*;

import java.util.*;

public class DTree {
    private Membership membership;
    private Set<String> alphabets;
    private List<String> sigma;
    private String name;
    private RTA hypothesis;
    private Node root = new Node(TimeWords.EMPTY_WORDS);
    private Map<TimeWords,Node> leafMap = new HashMap<>();

    private Set<TimeWord> inputWords = new HashSet<>();
    private boolean isComplete = false;
    private Set<Guard> guardSet = new HashSet<>();
    private List<Node> nodeList;
    private Map<TimeWords,Location> locationMap = new HashMap<>();

    public DTree(Set<String> alphabets,Membership membership,String name){
        this.alphabets = alphabets;
        this.sigma = new ArrayList<>(alphabets);
        this.name = name;
        this.membership = membership;

        for(String action: sigma){
            TimeWord word = new TimeWord(action,0);
            inputWords.add(word);
        }

        boolean emptyAccpted = membership.answer(TimeWords.EMPTY_WORDS);
        if(emptyAccpted == false){
            Node left = new Node(true,false,TimeWords.EMPTY_WORDS);
            root.setLeftChild(left);
        }else {
            Node right = new Node(true,true,TimeWords.EMPTY_WORDS);
            root.setRightChild(right);
        }
        refineGuard();
        nodeList = getleafs();

    }

    private void refineGuard(){
        refineMap();
        for(TimeWords sourceLabel: leafMap.keySet()){
            for(TimeWord w:inputWords){
                TimeWords words = TimeWordsUtil.concat(sourceLabel,w);
                Node target = sift(words);
                if(target != null){
                    TimeWords targetLabel = target.getSuffix();
                    Guard guard = new Guard(sourceLabel,targetLabel,w);
                    guardSet.add(guard);
                }
            }
        }
    }


    public RTA tranToRTA(){
        RTA evidRTA = tranToEvidRTA();
        RTA rta = RTAUtil.evidRTAToRTA(evidRTA);
        RTAUtil.refineTran(rta);
        return rta;
    }

    public RTA tranToEvidRTA(){
        refineMap();
        locationMap = new HashMap<>();
        List<Location> locations = new ArrayList<>();
        int len = nodeList.size();
        //构建LocationList
        for(int i = 0; i < len; i++){
            Node node = nodeList.get(i);
            int id = i+1;
            String nodeName = name+i;
            boolean accepted = node.isAccpted();
            boolean init = node.isInit();
            Location location = new Location(i+1,name+i,init,accepted);
            locationMap.put(node.getSuffix(),location);
            locations.add(location);
        }

        //构建TransitionList
        List<Transition> transitions = new ArrayList<>();
        for(Guard guard: guardSet){
            TimeWords sourceLabel = guard.getSourceLabel();
            TimeWords targetLabel = guard.getTargetLabel();
            Location sourceLocation = locationMap.get(sourceLabel);
            Location targetLocation = locationMap.get(targetLabel);
            TimeWord word = guard.getWord();
            String action = word.getAction();
            TimeGuard timeGuard = RTAUtil.toIntegerTimeGuard(word);
            Transition transition = new Transition(sourceLocation,targetLocation,timeGuard,action);
            transitions.add(transition);
        }
        return new RTA(name,alphabets,locations,transitions);
    }

    public void refine(TimeWords ce){
        List<TimeWord> wordList = ce.getWordList();
        inputWords.addAll(wordList);

        //如果二叉树由空叶子节点。
        if(!isComplete){
            Node pre = null;
            Node currentNode = root;
            while (!currentNode.isLeaf()){
                TimeWords suffix = currentNode.getSuffix();
                TimeWords timeWords = TimeWordsUtil.concat(ce,suffix);
                boolean answer = membership.answer(timeWords);
                pre = currentNode;
                Node left = currentNode.getLeftChild();
                Node right = currentNode.getRightChild();
                currentNode = answer?right:left;
                if(currentNode == null){
                    isComplete = true;
                    boolean init = ce.equals(TimeWords.EMPTY_WORDS);
                    boolean accepted = answer;
                    Node node = new Node(init,accepted,ce);
                    if(answer){
                        pre.setRightChild(node);
                    }else {
                        pre.setLeftChild(node);
                    }
                    refineGuard();
                    return;
                }
            }
        }

        //二叉树没有叶子节点为空的情况。
        TimeWords words1 = null;
        TimeWords words2 = null;
        int j;
        for(j = 0; j < ce.size(); j++){
            words1 = gama(ce,j);
            words2 = gama(ce,j+1);
            boolean var = membership.answer(words1) == membership.answer(words2);
            if(!var){
                break;
            }
        }
        TimeWords u = TimeWordsUtil.getFirstN(ce,j);
        Location qu = hypothesis.traceToLocationByTimeWord(u);
        TimeWord w = ce.get(j+1);
        TimeWords v = TimeWordsUtil.concat(u,w);
        Node node = sift(v);
        int id = nodeList.indexOf(node)+1;
        Location qv = hypothesis.getLocationById(id);
        List<Transition> transitionList = hypothesis.getTransitionsBetweenLocations(qu,qv);
        boolean isPass = false;
        for(Transition t:transitionList){
            if(t.isPass(w)){
                isPass = true;
                break;
            }
        }

        //更新guard
        if(!isPass){
            Guard guard = new Guard(u,v,w);
            guardSet.add(guard);
        }
        //增加状态
        else {
            Node replacedNode = getNode(v);
            TimeWords words = TimeWordsUtil.getLastByN(ce,j+1);
            replacedNode.setSuffix(words);
            TimeWords newWords1 = v;
            TimeWords newWords2 = TimeWordsUtil.concat(u,w);
            TimeWords answerWords = TimeWordsUtil.concat(newWords1,words);
            boolean var = membership.answer(answerWords);
            if(var){
                boolean init1 = newWords1.equals(TimeWords.EMPTY_WORDS);
                Node node1 = new Node(init1,true,newWords1);
                replacedNode.setRightChild(node1);
                boolean init2 = newWords2.equals(TimeWords.EMPTY_WORDS);
                Node node2 = new Node(init2,false,newWords2);
                replacedNode.setLeftChild(node2);
            }else {
                boolean init1 = newWords1.equals(TimeWords.EMPTY_WORDS);
                Node node1 = new Node(init1,false,newWords1);
                replacedNode.setLeftChild(node1);
                boolean init2 = newWords2.equals(TimeWords.EMPTY_WORDS);
                Node node2 = new Node(init2,true,newWords2);
                replacedNode.setRightChild(node2);
            }

            //更新到v节点的迁移
            for(Node l:nodeList){
                TimeWords lWords = l.getSuffix();
                List<Guard> guardList = getGuardsBetweenTimeWords(lWords,v);
                for(Guard guard:guardList){
                    TimeWord word = guard.getWord();
                    TimeWords prefix = TimeWordsUtil.concat(lWords,word);
                    TimeWords rWords = TimeWordsUtil.concat(prefix,words);
                    if(membership.answer(rWords)){
                        TimeWords target = replacedNode.getRightChild().getSuffix();
                        guard.setTargetLabel(target);
                    }else {
                        TimeWords target = replacedNode.getLeftChild().getSuffix();
                        guard.setTargetLabel(target);
                    }
                }
            }

            //更新以新增节点为出发点的迁移
            for(String action: sigma){
                TimeWord word = new TimeWord(action,0);
                TimeWords sourceLabel = newWords2;
                TimeWords sWords = TimeWordsUtil.concat(sourceLabel,word);
                TimeWords targetLabel = sift(sWords).getSuffix();
                Guard guard = new Guard(sourceLabel,targetLabel,word);
                guardSet.add(guard);
            }
        }
    }

    private List<Guard> getGuardsBetweenTimeWords(TimeWords words1, TimeWords words2){
        List<Guard> guards = new ArrayList<>();
        for(Guard guard:guardSet){
            if(guard.getSourceLabel().equals(words1) && guard.getTargetLabel().equals(words2)){
                guards.add(guard);
            }
        }
        return guards;
    }

    private TimeWords gama(TimeWords words, int j){
        TimeWords w = TimeWordsUtil.getFirstN(words,j);
        Location location = hypothesis.traceToLocationByTimeWord(w);
        int index = location.getId()-1;
        TimeWords prefix = getleafs().get(index).getSuffix();
        TimeWords suffix = TimeWordsUtil.getLastByN(words, j);
        TimeWords timeWords = TimeWordsUtil.concat(prefix,suffix);
        return timeWords;
    }


    public Node sift(TimeWords words){
        Node currentNode = root;
        while (currentNode != null && !currentNode.isLeaf()){
            TimeWords suffix = currentNode.getSuffix();
            TimeWords timeWords = TimeWordsUtil.concat(words,suffix);
            boolean answer = membership.answer(timeWords);
            if(answer){
                currentNode = currentNode.getRightChild();
            }else {
                currentNode = currentNode.getLeftChild();
            }
        }
        return currentNode;
    }

    public Node getRoot(){
        return root;
    }

    public void refineMap(){
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            Node node = queue.remove();
            if(node.isLeaf()){
                TimeWords suffix= node.getSuffix();
                leafMap.put(suffix,node);
            }
            else {
                Node left = root.getLeftChild();
                Node right = root.getRightChild();
                if(left!=null){
                    queue.add(left);
                }
                if(right!=null){
                    queue.add(right);
                }
            }
        }
        nodeList = getleafs();
    }


    public Node getNode(TimeWords key){
        return leafMap.get(key);
}


    public int leafnum(){
        return leafMap.size();
    }



    public List<Node> getleafs(){
        return new ArrayList<>(leafMap.values());
    }

    @Override
    public String toString(){
        return show(root, 0);
    }

    public String show(Node node, int level){
        String s = "";
        for(int i = 0; i < level; i ++){
            s+="---";
        }
        s+=node.toString();
        s+="\n";
        Node left = node.getLeftChild();
        Node right = node.getRightChild();
        if(left!=null){
            s+=show(left, level+1);
        }
        if(right!=null){
            s+=show(right, level+1);
        }
        return s;
    }

    //    //获取叶子节点
//    public Node getNode(TimeWords key){
//        Deque<Node> stack = new LinkedList<>();
//        Node node = root;
//        if(node.getLeftChild()!=null){
//            stack.push(node.getLeftChild());
//        }
//        if(node.getRightChild()!=null){
//            stack.push(node.getRightChild());
//        }
//
//        Node current = null;
//        while(!stack.isEmpty()){
//            current = stack.pop();
//            TimeWords words= current.getSuffix();
//            if(words.equals(key) && current.isLeaf()){
//                return current;
//            }
//            Node left = current.getLeftChild();
//            Node right = current.getRightChild();
//            if(left!=null){
//                stack.push(left);
//            }
//            if(right!=null){
//                stack.push(right);
//            }
//        }
//        return null;
//    }
    //    public HashSet<Node> getleafs(){
//        HashSet<Node> set = new HashSet<>();
//        Node root = getRoot();
//        LinkedList<Node> queue = new LinkedList<>();
//        queue.add(root);
//        while(!queue.isEmpty()){
//            Node node = queue.remove();
//            Node left = root.getLeftChild();
//            Node right = root.getRightChild();
//            if(left!=null){
//                queue.add(left);
//            }
//            if(right!=null){
//                queue.add(right);
//            }
//            if(node.isLeaf()){
//                set.add(node);
//            }
//        }
//        return set;
//    }
    //    private int leafnum(Node root){
//        Node left = root.getLeftChild();
//        Node right = root.getRightChild();
//        if(root==null){
//            return 0;
//        }
//        if(left==null && right==null){
//            return 1;
//        }
//        return leafnum(left)+leafnum(right);
//
//    }
//
//    public int leafnum(){
//        return leafnum(getRoot());
//    }
}
