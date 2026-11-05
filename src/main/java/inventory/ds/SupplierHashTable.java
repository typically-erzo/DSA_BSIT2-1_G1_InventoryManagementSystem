package inventory.ds;

import inventory.model.Supplier;
import java.util.LinkedList;
import java.util.List;

public class SupplierHashTable {
    private static final int CAPACITY = 10;
    private final LinkedList<Supplier>[] table;

    @SuppressWarnings("unchecked")
    public SupplierHashTable() {
        table = new LinkedList[CAPACITY];
        for (int i = 0; i < CAPACITY; i++) {
            table[i] = new LinkedList<>();
        }
    }

    private int hash(String key) {
        return Math.abs(key.hashCode()) % CAPACITY;
    }

    public void put(Supplier supplier) {
        int index = hash(supplier.getId());
        for (Supplier s : table[index]) {
            if (s.getId().equals(supplier.getId())) {
                s.setName(supplier.getName());
                s.setContactInfo(supplier.getContactInfo());
                s.setAddress(supplier.getAddress());
                return;
            }
        }
        table[index].add(supplier);
    }

    public Supplier get(String id) {
        int index = hash(id);
        for (Supplier s : table[index]) {
            if (s.getId().equals(id)) {
                return s;
            }
        }
        return null;
    }

    public boolean remove(String id) {
        int index = hash(id);
        for (Supplier s : table[index]) {
            if (s.getId().equals(id)) {
                table[index].remove(s);
                return true;
            }
        }
        return false;
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> all = new LinkedList<>();
        for (LinkedList<Supplier> bucket : table) {
            all.addAll(bucket);
        }
        return all;
    }
}

