package uppaalRTA;



import learning.EquivalenceQuery;
import rta.RTA;
import rta.RTAUtil;
import rta.TimeWord;
import rta.TimeWords;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class UppaalEq{
    private RTA m1;
    private RTA m2;
    String property;
    public UppaalEq(RTA m1, RTA m2, String property) {
        this.m1 = m1;
        this.m2 = m2;
        this.property = property;
    }

    public TimeWords findCounterEa(RTA assume){
        UppaalNTA uppaalNTA = new UppaalNTA(assume,m1);
        uppaalNTA.toXml();
        generateProperty();
        uppaalNTA.toBat();
        if(UppaalCheck.isSatisFied()){
            return null;
        }
        else {
            try{
                System.out.println("请给出一个反例：先给出反例长度n，再给出n行输入：");
                BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
                int n = Integer.parseInt(rd.readLine());
                List<TimeWord> words = new ArrayList<>();
                for(int i = 0; i < n ;i++){
                    String[] str = rd.readLine().split(",");
                    TimeWord word = new TimeWord(str[0],Double.parseDouble(str[1]));
                    words.add(word);
                }
                TimeWords timeWords = new TimeWords(words);
                return timeWords;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    private void generateProperty(){
        OutputStreamWriter writer = null;
        try{
            writer = new OutputStreamWriter(new FileOutputStream(".//uppaalNta.q"));
            writer.write(property);
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public TimeWords findCounterExample(RTA assumpthion) {
        RTA completeAssumption = RTAUtil.toCompleteRTA(assumpthion);
        UppaalNTA uppaalNTA = new UppaalNTA(m2,completeAssumption);
        uppaalNTA.toXml();
        uppaalNTA.generateMemProperty();
        uppaalNTA.toBat();
        if(UppaalCheck.isSatisFied()){
            System.out.println("找到假设成功");
            System.out.println(assumpthion);
            return null;
        }
        else {
            try{
                System.out.println("请给出一个反例：先给出反例长度n，再给出n行输入：");
                BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));
                int n = Integer.parseInt(rd.readLine());
                List<TimeWord> words = new ArrayList<>();
                for(int i = 0; i < n ;i++){
                    String[] str = rd.readLine().split(",");
                    TimeWord word = new TimeWord(str[0],Double.parseDouble(str[1]));
                    words.add(word);
                }
                TimeWords timeWords = new TimeWords(words);
                return timeWords;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
