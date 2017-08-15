package com.example.alejandro.jmr_android.jmr;

/**
 * Created by alejandro on 15/08/2017.
 */

public class ResultMetadata<R,T> implements Comparable<ResultMetadata> {
    private R result;
    private T metadata;

    public ResultMetadata(R result, T metadata){
        this.result = result;
        this.metadata = metadata;
    }

    public R getResult(){
        return result;
    }

    public void setResult(R result){
        this.result = result;
    }

    public T getMetadata() {
        return metadata;
    }

    public void setMetadata(T metadata) {
        this.metadata = metadata;
    }

    @Override
    public int compareTo(ResultMetadata o) {
        return ((Comparable)result).compareTo(o.result);
    }
}
