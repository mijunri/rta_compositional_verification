package learning.ttt;

import rta.TimeWord;
import rta.TimeWords;

public class Guard {
    private TimeWords sourceLabel;
    private TimeWords targetLabel;
    private TimeWord word;


    public Guard(TimeWords sourceLabel, TimeWords targetLabel, TimeWord word) {
        this.sourceLabel = sourceLabel;
        this.targetLabel = targetLabel;
        this.word = word;
    }

    public TimeWords getSourceLabel() {
        return sourceLabel;
    }

    public void setSourceLabel(TimeWords sourceLabel) {
        this.sourceLabel = sourceLabel;
    }

    public TimeWords getTargetLabel() {
        return targetLabel;
    }

    public void setTargetLabel(TimeWords targetLabel) {
        this.targetLabel = targetLabel;
    }

    public TimeWord getWord() {
        return word;
    }

    public void setWord(TimeWord word) {
        this.word = word;
    }

    @Override
    public int hashCode(){
        return sourceLabel.hashCode()+targetLabel.hashCode()+word.hashCode();
    }

    @Override
    public boolean equals(Object o){
        Guard guard = (Guard)o;
        boolean var1 = sourceLabel.equals(guard.sourceLabel);
        boolean var2 = targetLabel.equals(guard.targetLabel);
        boolean var3 = word.equals(guard.word);
        return var1 && var2 && var3;
    }
}
