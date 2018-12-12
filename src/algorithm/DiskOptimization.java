package algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;


public class DiskOptimization {
    private static ArrayList resultData = new ArrayList();
    static ArrayList getResult(){
        return resultData;
    }
    private DiskParameter dp = null;

    public static void main(String[] args){
        new DiskOptimization("diskq1.properties");
        new DiskOptimizationUI();
    }

    public DiskOptimization(String filename){
        try {
            Properties p = new Properties();
            p.load(new BufferedReader(new FileReader(filename)));
            dp = new DiskParameter(p);
        } catch (Exception e){
            e.printStackTrace();
        }
        generateAnalysis();
        //call in main method to generate the print below
    }

    private void generateAnalysis(){
        generateFCFS();
        generateSSTF();
        generateScan();
        generateLook();
        generateCScan();
        generateCLook();
        // call all the algorithm
    }

    private void printSequence(String name, int[] location){
        StringBuilder sequence = new StringBuilder();
        StringBuilder working1 = new StringBuilder();
        StringBuilder working2 = new StringBuilder();
        int total = 0;
        sequence.append(dp.getCurrent());

        int previous = dp.getCurrent();
        for (int current : location) {
            sequence.append(",").append(current);
            int d = Math.abs(previous - current);
            working1.append("|").append(previous).append("-").append(current).append("|+");
            working2.append(d).append(" + ");
            total += d;
            previous = current;
        }
        String result ="\n" + name + "\n" + "====================" +
                "\nOrder of Access: " + sequence +
                "\nTotal Distance = " + working1.substring(0, working1.length()-1) +
                "\n               = " + working2.substring(0, working2.length()-2) +
                "\n               = " + total + '\n';
        System.out.println(result);
        resultData.add(result);
    }

    private void printSequenceCScan(String name, int[] location){
        StringBuilder sequence = new StringBuilder();
        StringBuilder working1 = new StringBuilder();
        StringBuilder working2 = new StringBuilder();
        int total = 0;
        sequence.append(dp.getCurrent());

        int cylinder = dp.getCylinders()-1;
        //get cylinder
        int previous = dp.getCurrent();
        for (int current : location) {
            sequence.append(",").append(current);
            int d = Math.abs(previous - current);
            working1.append("|").append(previous).append("-").append(current).append("|+");
            working2.append(d).append(" + ");
            total += d;
            previous = current;
        }
        String result ="\n" + name + "\n" + "====================" +
                "\nOrder of Access: " + sequence +
                "\nTotal Distance = " + working1.substring(0, working1.length()-1) +"-"+cylinder+
                "\n               = " + working2.substring(0, working2.length()-2) +"- "+cylinder+
                "\n               = " + total +" - "+cylinder+
                "\n               = " + (total-cylinder)+ '\n';

        System.out.println(result);
        resultData.add(result);
    }

    private void printSequenceCLook(String name, int[] location){
        StringBuilder sequence = new StringBuilder();
        StringBuilder working1 = new StringBuilder();
        StringBuilder working2 = new StringBuilder();
        int total = 0;
        sequence.append(dp.getCurrent());

        int min = Arrays.stream(location).min().getAsInt();
        int max = Arrays.stream(location).max().getAsInt();
        //get min and max

        int ignore = max - min;
        //the ignore calculation

        int previous = dp.getCurrent();
        for (int current : location) {
            sequence.append(",").append(current);
            int d = Math.abs(previous - current);
            working1.append("|").append(previous).append("-").append(current).append("|+");
            working2.append(d).append(" + ");
            total += d;
            previous = current;
        }
        String result ="\n" + name + "\n" + "====================" +
                "\nOrder of Access: " + sequence +
                "\nTotal Distance = " + working1.substring(0, working1.length()-1) +"-"+ignore+
                "\n               = " + working2.substring(0, working2.length()-2) +"- "+ignore+
                "\n               = " + total +"- "+ignore+
                "\n               = " + (total-ignore) + '\n';
        System.out.println(result);
        resultData.add(result);
    }

    private void generateFCFS(){
        int[] location = dp.getSequence();
//        for (int aLocation : location) {
//            System.out.println(aLocation);
//        }
        //FCFS no need to change arrangement, directly return the same array
        printSequence("FCFS", location);
    }

    private int[] arrangeBySSTF(int current, int[] sequence){
        int n = sequence.length;
        int[] sstf = new int[n];
        //get the new array by new array sstf
        System.arraycopy(sequence, 0, sstf, 0, n);
        int ii;
        for(int i=0;i<n;i++){
            int minimun = dp.getCylinders();
            //let the largest number as initial min
            ii = i;
            for (int j=i;j<n;j++){
                //this loop start from the current assign index i to check every num after
                int distance = Math.abs(current - sstf[j]);
                if (distance < minimun){
                    ii = j;
                    minimun = distance;
                }
            }
            //this determine the num with least distance and assign ii to the index
            int temp = sstf[i];
            sstf[i] = sstf[ii];
            sstf[ii] = temp;
            current = sstf[i];
            //similar to selection sort
            //set the current i index with the num with found least distance ii
            //and the for loop going on to check the remaining positions
        }
        return sstf;
    }

    private void generateSSTF(){
        int[] location = arrangeBySSTF(dp.getCurrent(), dp.getSequence());
        printSequence("SSTF", location);
    }

    private int[] arrangeByScan(int current, int[] sequence) {
        int n = sequence.length; // get sequence size
        int[] scan = new int[n + 1];
        // copy from properties to temp scan array
        List<Integer> tempScan = new ArrayList<Integer>();
        //copy the end cylinder to array list
        for (int i : sequence) {
            tempScan.add(i);
        }
        List<Integer> before = new ArrayList<Integer>();
        List<Integer> after = new ArrayList<Integer>();
        if (dp.getPrevious() <= dp.getCurrent()) {
            tempScan.add(dp.getCylinders()-1);
            Collections.sort(tempScan);
            // separate into before and after
            for (int i=0;i<n+1;i++){
                if(tempScan.get(i)>=dp.getCurrent()){
                    before.add(tempScan.get(i));
                }else{
                    after.add(tempScan.get(i));
                }
            }
            Collections.reverse(after);
            //reverse the after part sequence
        } else {
            tempScan.add(0);
            Collections.sort(tempScan);
            for (int i=0;i<n+1;i++){
                if(tempScan.get(i)<dp.getCurrent()){
                    before.add(tempScan.get(i));
                }else{
                    after.add(tempScan.get(i));
                }
            }
            Collections.reverse(before);
        }
        before.addAll(after);
        // concatinate both before and after
        for (int i = 0; i < before.size(); i++) {
            scan[i] = before.get(i);
        }
        //assign arrayList back to array
        return scan;
    }

    private void generateScan(){
        int[] location = arrangeByScan(dp.getCurrent(), dp.getSequence());
        printSequence("SCAN", location);
    }

    private int[] arrangeByLook(int current, int[] sequence) {
        int n = sequence.length;
        int[] look = new int[n];
        List<Integer> tempLook = new ArrayList<Integer>();
        for (int i : sequence) {
            tempLook.add(i);
        }
        List<Integer> before = new ArrayList<Integer>();
        List<Integer> after = new ArrayList<Integer>();
        if (dp.getPrevious() <= dp.getCurrent()) {
            Collections.sort(tempLook);
            for (int i=0;i<n;i++){
                if(tempLook.get(i)>=dp.getCurrent()){
                    before.add(tempLook.get(i));
                }else{
                    after.add(tempLook.get(i));
                }
            }
            Collections.reverse(after);
        } else {
            Collections.sort(tempLook);
            for (int i=0;i<n;i++){
                if(tempLook.get(i)<dp.getCurrent()){
                    before.add(tempLook.get(i));
                }else{
                    after.add(tempLook.get(i));
                }
            }
            Collections.reverse(before);
        }
        before.addAll(after);
        for (int i = 0; i < before.size(); i++) {
            look[i] = before.get(i);
        }
        return look;
    }

    private void generateLook(){
        int[] location = arrangeByLook(dp.getCurrent(), dp.getSequence());
        printSequence("LOOK", location);
    }

    private int[] arrangeByCScan(int current, int[] sequence) {
        int n = sequence.length; // get sequence size
        int[] scan = new int[n + 2];
        // !! important the size is 2 more than the default
        // copy from properties to temp scan array
        List<Integer> temp = new ArrayList<Integer>();
        //copy the end cylinder to array list
        for (int i : sequence) {
            temp.add(i);
        }
        List<Integer> before = new ArrayList<Integer>();
        List<Integer> after = new ArrayList<Integer>();
        temp.add(dp.getCylinders()-1);
        temp.add(0);
        Collections.sort(temp);
        if (dp.getPrevious() <= dp.getCurrent()) {
            // separate into before and after
            for (int i=0;i<n+2;i++){
                if(temp.get(i)>dp.getCurrent()){
                    before.add(temp.get(i));
                }else{
                    after.add(temp.get(i));
                }
            }
            // no need reverse the after part sequence
        } else {
            for (int i=0;i<n+2;i++){
                if(temp.get(i)<dp.getCurrent()){
                    before.add(temp.get(i));
                }else{
                    after.add(temp.get(i));
                }
            }
            Collections.reverse(before);
            Collections.reverse(after);
            //both is descending
        }
        before.addAll(after);
        // concatinate both before and after
        for (int i = 0; i < before.size(); i++) {
            scan[i] = before.get(i);
        }
        //assign arrayList back to array
        return scan;
    }

    //method 2
    private int[] cscan(){
        int [] sequence=dp.getSequence();
        List<Integer> aSeq = new ArrayList<>();
        for (int i1 : sequence) {
            aSeq.add(i1);
        }
        aSeq.add(dp.getCurrent());
        aSeq.add(0);
        aSeq.add(dp.getCylinders()-1);
        Collections.sort((aSeq));
        if(dp.getPrevious()>=dp.getCurrent()){
            Collections.reverse(aSeq);
        }
        int currentCylinderIndex = aSeq.indexOf(dp.getCurrent());
        List<Integer> scanSpliceArr1 = aSeq.subList(0,currentCylinderIndex);
        List<Integer> scanSpliceArr2 = aSeq.subList(currentCylinderIndex,aSeq.size());
        scanSpliceArr2.addAll(scanSpliceArr1);
        scanSpliceArr2.remove(0);
        int[] cscan = new int[sequence.length+2];
        for(int i=0;i<scanSpliceArr2.size();i++){
            cscan[i]=scanSpliceArr2.get(i);
        }
        return  cscan;
        // the difference is get all the numbers include current in to an array list
        // then sort the array list and substring base on the index of current
        // last remove the current num and concatenate together to form the sequence
    }

    private void generateCScan(){
        int[] location = cscan();
        printSequenceCScan("C-SCAN", location);
    }

    private int[] arrangeByCLook(int current, int[] sequence) {
        int n = sequence.length;
        int[] look = new int[n];
        List<Integer> temp = new ArrayList<Integer>();
        for (int i : sequence) {
            temp.add(i);
        }
        List<Integer> before = new ArrayList<Integer>();
        List<Integer> after = new ArrayList<Integer>();
        Collections.sort(temp);
        if (dp.getPrevious() <= dp.getCurrent()) {
            for (int i=0;i<n;i++){
                if(temp.get(i)>=dp.getCurrent()){
                    before.add(temp.get(i));
                }else{
                    after.add(temp.get(i));
                }
            }
        } else {
            for (int i=0;i<n;i++){
                if(temp.get(i)<=dp.getCurrent()){
                    before.add(temp.get(i));
                }else{
                    after.add(temp.get(i));
                }
            }
            Collections.reverse(before);
            Collections.reverse(after);
        }
        before.addAll(after);
        for (int i = 0; i < before.size(); i++) {
            look[i] = before.get(i);
        }
        return look;
    }

    private void generateCLook(){
        int[] location = arrangeByCLook(dp.getCurrent(), dp.getSequence());
        printSequenceCLook("C-LOOK", location);
    }


}
