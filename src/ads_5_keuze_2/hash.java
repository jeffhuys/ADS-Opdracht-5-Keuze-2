package ads_5_keuze_2;

import java.io.*;
import java.util.ArrayList;

class DataItem {

    private int iData;

    public DataItem(int ii)
    {
        iData = ii;
    }

    public int getKey() {
        return iData;
    }
}

class HashTable {

    private DataItem[] hashArray;
    private int arraySize;
    private DataItem nonItem;
    
    private final int LINEAR_PROBING = 0;
    private final int QUADRATIC_PROBING = 1;
    private final int DOUBLE_HASHING = 2;

    public HashTable(int size)
    {
        arraySize = size;
        hashArray = new DataItem[arraySize];
        nonItem = new DataItem(-1);
    }

    public int get(int i) {
        if (hashArray[i] != null) {
            return hashArray[i].getKey();
        } else {
            return -1;
        }
    }

    public int size() {
        return arraySize;
    }

    public void displayTable() {
        System.out.print("Table: ");
        for (int j = 0; j < arraySize; j++) {
            if (hashArray[j] != null) {
                System.out.print(hashArray[j].getKey() + " ");
            } else {
                System.out.print("** ");
            }
        }
        System.out.println("");
    }

    public int hashFunc(int key) {
        return key % arraySize;
    }

    public int hashFunc2(int key) {
        return 5 - key % 5;
    }

    public int insert(DataItem item, int type)
    {
        int key = item.getKey();
        int pos = 0;
        int hashVal = hashFunc(key);
        int collisions = 0;
        int stepSize = hashFunc2(key);
        while (hashArray[hashVal] != null && hashArray[hashVal].getKey() != -1) {
            if (type == LINEAR_PROBING) {
                hashVal++;

            } else if (type == QUADRATIC_PROBING) {
                hashVal += Math.pow(pos, 2);
                pos++;

            } else if (type == DOUBLE_HASHING) {
                hashVal += stepSize;
            }
            hashVal %= arraySize;
            collisions++;
        }
        hashArray[hashVal] = item;
        return collisions;
    }

    public DataItem delete(int key, int type)
    {
        int hashVal = hashFunc(key);
        int pos = 0;
        int stepSize = hashFunc2(key);

        while (hashArray[hashVal] != null)
        {
            if (hashArray[hashVal].getKey() == key) {
                DataItem temp = hashArray[hashVal];
                hashArray[hashVal] = nonItem;
                return temp;
            }
            if (type == LINEAR_PROBING) {
                hashVal++;
            } else if (type == QUADRATIC_PROBING) {
                hashVal += Math.pow(pos, 2);
                pos++;
            } else if (type == DOUBLE_HASHING) {
                hashVal += stepSize;
            }

            hashVal %= arraySize;
        }
        return null;
    }

    public DataItem find(int key, int type)
    {
        int hashVal = hashFunc(key);
        int pos = 0;
        int stepSize = hashFunc2(key);

        while (hashArray[hashVal] != null)
        {
            if (hashArray[hashVal].getKey() == key) {
                return hashArray[hashVal];
            }
            if (type == LINEAR_PROBING) {
                hashVal++;
            } else if (type == QUADRATIC_PROBING) {
                hashVal += Math.pow(pos, 2);
                pos++;
            } else if (type == DOUBLE_HASHING) {
                hashVal += stepSize;
            }
            hashVal %= arraySize;
        }
        return null;
    }

    public int findCollisions(int key, int type) {
        int hashVal = hashFunc(key); 
        int collisions = 0;
        int pos = 0;
        int stepSize = hashFunc2(key);

        while (hashArray[hashVal] != null)
        {
            if (hashArray[hashVal].getKey() == key) {
                return collisions;
            }
            if (type == LINEAR_PROBING) {
                hashVal++;
            } else if (type == QUADRATIC_PROBING) {
                hashVal += Math.pow(pos, 2);
                pos++;
            } else if (type == DOUBLE_HASHING) {
                hashVal += stepSize;
            }


            hashVal %= arraySize;
            collisions++;
        }
        return collisions;
    }
}

class HashTableApp {

    public static void main(String[] args) throws IOException {
        DataItem aDataItem;
        int aKey, size, n, keysPerCell;
        String insertString = "    65%  75%  85%   95% \n";
        String searchString = "    65%   75%   85%   95% \n";

        int calcType = 0;

        for (calcType = 0; calcType < 3; calcType++) {

            switch (calcType) {
                case 0:
                    insertString += "LP ";
                    searchString += "LP ";
                    break;
                case 1:
                    insertString += "QP ";
                    searchString += "QP ";
                    break;
                case 2:
                    insertString += "DH ";
                    searchString += "DH ";
                    break;
            }

            size = 4999;

            int fillSize = 0;
            HashTable theHashTable = null;
            HashTable searchHashTable = null;

            ArrayList<HashTable> insertTables = new ArrayList<>();
            ArrayList<HashTable> searchTables = new ArrayList<>();

            n = size;

            keysPerCell = 10;

            int counter = 0;
            while (counter < 4) {
                switch (counter) {
                    case 0:
                        fillSize = (size / 100) * 65;
                        break;
                    case 1:
                        fillSize = (size / 100) * 75;
                        break;
                    case 2:
                        fillSize = (size / 100) * 85;
                        break;
                    case 3:
                        fillSize = (size / 100) * 95;
                        break;
                }
                counter++;
                
                theHashTable = new HashTable(size);
                searchHashTable = new HashTable(size);

                int collisions = 0;

                for (int j = 0; j < fillSize; j++)
                {
                    aKey = (int) (java.lang.Math.random() * keysPerCell * size);
                    aDataItem = new DataItem(aKey);
                    collisions += theHashTable.insert(aDataItem, calcType);
                    // Tot de helft OOK in de searchTable
                    if (j <= size / 2) {
                        searchHashTable.insert(aDataItem, calcType);
                    } else {
                        searchHashTable.insert(new DataItem((int) (java.lang.Math.random() * keysPerCell * size)), calcType);
                    }
                }
                insertString += " " + collisions + " ";
                insertTables.add(theHashTable);
                searchTables.add(searchHashTable);
            }

            insertString += "\n";

            // We gaan in de insertTables zoeken naar getallen die in de searchTables staan
            int count = 0;
            int collisions = 0;
            for (HashTable searchTable : searchTables) {
                collisions = 0;
                for (int i = 0; i < searchTable.size(); i++) {
                    if (searchTable.get(i) != -1) {
                        collisions += insertTables.get(count).findCollisions(searchTable.get(i), calcType);
                    }
                }
                searchString += " " + collisions + " ";
                count++;
            }

            searchString += "\n";
        }

        System.out.println(insertString);
        System.out.println(searchString);
    }

    public static String getString() throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String s = br.readLine();
        return s;
    }

    public static char getChar() throws IOException {
        String s = getString();
        return s.charAt(0);
    }

    public static int getInt() throws IOException {
        String s = getString();
        return Integer.parseInt(s);
    }
} 