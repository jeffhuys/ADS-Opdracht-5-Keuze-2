package ads_5_keuze_2;

// hash.java
// demonstrates hash table with linear probing
// to run this program: C:>java HashTableApp
import java.io.*;
import java.util.ArrayList;

class DataItem {                                // (could have more data)

    private int iData;               // data item (key)

    public DataItem(int ii) // constructor
    {
        iData = ii;
    }

    public int getKey() {
        return iData;
    }
}  // end class DataItem

class HashTable {

    private DataItem[] hashArray;    // array holds hash table
    private int arraySize;
    private DataItem nonItem;        // for deleted items

    public HashTable(int size) // constructor
    {
        arraySize = size;
        hashArray = new DataItem[arraySize];
        nonItem = new DataItem(-1);   // deleted item key is -1
    }

    public int get(int i) {
        if(hashArray[i] != null) {
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
        return key % arraySize;       // hash function
    }

    public int insert(DataItem item) // insert a DataItem
    // (assumes table not full)
    {
        int key = item.getKey();      // extract key
        int hashVal = hashFunc(key);  // hash the key
        int collisions = 0;
        // until empty cell or -1,
        while (hashArray[hashVal] != null
                && hashArray[hashVal].getKey() != -1) {
            ++hashVal;                 // go to next cell
            hashVal %= arraySize;      // wraparound if necessary

            collisions++;
        }
        hashArray[hashVal] = item;    // insert item
        return collisions;
    }  // end insert()

    public DataItem delete(int key) // delete a DataItem
    {
        int hashVal = hashFunc(key);  // hash the key

        while (hashArray[hashVal] != null) // until empty cell,
        {                               // found the key?
            if (hashArray[hashVal].getKey() == key) {
                DataItem temp = hashArray[hashVal]; // save item
                hashArray[hashVal] = nonItem;       // delete item
                return temp;                        // return item
            }
            ++hashVal;                 // go to next cell
            hashVal %= arraySize;      // wraparound if necessary
        }
        return null;                  // can't find item
    }  // end delete()

    public DataItem find(int key) // find item with key
    {
        int hashVal = hashFunc(key);  // hash the key
        int collisions = 0;

        while (hashArray[hashVal] != null) // until empty cell,
        {                               // found the key?
            if (hashArray[hashVal].getKey() == key) {
                return hashArray[hashVal];   // yes, return item
            }
            ++hashVal;                 // go to next cell
            hashVal %= arraySize;      // wraparound if necessary
        }
        return null;                  // can't find item
    }

    public int findCollisions(int key) {
        int hashVal = hashFunc(key);  // hash the key
        int collisions = 0;

        while (hashArray[hashVal] != null) // until empty cell,
        {                               // found the key?
            if (hashArray[hashVal].getKey() == key) {
                //return hashArray[hashVal];   // yes, return item
            }
            ++hashVal;                 // go to next cell
            hashVal %= arraySize;      // wraparound if necessary
            collisions++;
        }
        return collisions;                  // can't find item
    }
}  // end class HashTable

class HashTableApp {

    public static void main(String[] args) throws IOException {
        DataItem aDataItem;
        int aKey, size, n, keysPerCell;

        size = 1009;

        int percentage = (size / 100) * 65;
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
                    percentage = (size / 100) * 65;
                    break;
                case 1:
                    percentage = (size / 100) * 75;
                    break;
                case 2:
                    percentage = (size / 100) * 85;
                    break;
                case 3:
                    percentage = (size / 100) * 95;
                    break;
            }
            counter++;

            int[] array = new int[percentage];

            // make table
            theHashTable = new HashTable(size);
            searchHashTable = new HashTable(size);

            int collisions = 0;

            for (int j = 0; j < percentage; j++) // insert data
            {
                aKey = (int) (java.lang.Math.random() * keysPerCell * size);
                aDataItem = new DataItem(aKey);
                collisions += theHashTable.insert(aDataItem);
                // Tot de helft OOK in de searchTable
                if (j <= size / 2) {
                    searchHashTable.insert(aDataItem);
                } else {
                    searchHashTable.insert(new DataItem((int) (java.lang.Math.random() * keysPerCell * size)));
                }
            }
            System.out.println("Collisions bij insert: " + collisions);

            insertTables.add(theHashTable);
            searchTables.add(searchHashTable);
        }


        // We gaan in de insertTables zoeken naar getallen die in de searchTables staan
        int count = 0;
        int collisions = 0;
        for (HashTable searchTable : searchTables) {
            collisions = 0;
            for (int i = 0; i < searchTable.size(); i++) {
                if(searchTable.get(i) != -1)
                    collisions += insertTables.get(count).findCollisions(searchTable.get(i));
            }
            System.out.println("Collisions on find (" + count + "): " + collisions);
            count++;
        }



        //System.out.print("Collisions: ");
        //System.out.println(insertTables.get(3).insert(new DataItem(55555)));






        /*
         while (true) // interact with user
         {
         System.out.print("Enter first letter of ");
         System.out.print("show, insert, delete, or find: ");
         char choice = getChar();
         switch (choice) {
         case 's':
         theHashTable.displayTable();
         break;
         case 'i':
         System.out.print("Enter key value to insert: ");
         aKey = getInt();
         aDataItem = new DataItem(aKey);
         theHashTable.insert(aDataItem);
         break;
         case 'd':
         System.out.print("Enter key value to delete: ");
         aKey = getInt();
         theHashTable.delete(aKey);
         break;
         case 'f':
         System.out.print("Enter key value to find: ");
         aKey = getInt();
         aDataItem = theHashTable.find(aKey);
         if (aDataItem != null) {
         System.out.println("Found " + aKey);
         } else {
         System.out.println("Could not find " + aKey);
         }
         break;
         default:
         System.out.print("Invalid entry\n");
         }  // end switch
         }  // end while
         */
    }  // end main()

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
//-------------------------------------------------------------

    public static int getInt() throws IOException {
        String s = getString();
        return Integer.parseInt(s);
    }
}  // end class HashTableApp

