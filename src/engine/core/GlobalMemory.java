package engine.core;

public class GlobalMemory {
        private static class CustomMap{
        private class Node{
            private String variableName;
            private String dataValue;
            private Node next;

            public Node(String variableName,String dataValue){
                this.variableName = variableName;
                this.dataValue = dataValue;
            }
        }
        private int capacity;
        private Node[] slots;

        public CustomMap(){
            this.capacity = 16;
            this.slots = new Node[capacity];
        }
        private int hash(String key){
            int sum = 0;
            for(char x : key.toCharArray()){
                sum+=x;
            }
            return sum % capacity;
        }

        private void put(String key, String value){
            Node node = new Node(key, value);
            int index = hash(key);
            if(slots[index] == null){ 
                slots[index] = node;
                return;
            }
            Node ptr = slots[index];
            while(ptr != null){
                if(ptr.variableName.equals(key)){
                    ptr.dataValue = value;
                    return;
                }
                if(ptr.next == null){
                    break;
                }
                ptr = ptr.next;
            }
            ptr.next = node;
        }
        private String get(String key){
            int index = hash(key);
            if(slots[index] == null){
                return "NoT_FoUnD";
            }
            Node ptr = slots[index];
            while(ptr != null){
                if(ptr.variableName.equals(key)){
                    return ptr.dataValue;
                }
                ptr = ptr.next;
            }
            return "Not Found";
        }
    }
    public static void main(String args[]){
        CustomMap map = new CustomMap();
        map.put("Research", "The Story about Akshat Gupta is ....");
        map.put("Query", "Who is Akshat Gupta?");
        System.out.println(map.get("Query"));
        System.out.println(map.get("Research"));
        map.put("Query", "Refine it" + ", Context : " + map.get("Research"));
        map.put("Research", "Akshat Gupta is ....");
        System.out.println(map.get("Query"));
        System.out.println(map.get("Research"));
    }
}
