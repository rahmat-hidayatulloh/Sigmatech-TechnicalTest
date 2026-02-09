import java.util.*;
import java.util.stream.Collectors;

public class SigmatechBackend {

    // Main Execution
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== JAVA BACKEND TEST SOLUTIONS ===");
        System.out.println("1. Run with hardcoded examples");
        System.out.println("2. Run with manual input");
        System.out.print("Choose option (1/2): ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        
        if (choice == 1) {
            executeHardcoded();
        } else if (choice == 2) {
            executeManual(scanner);
        } else {
            System.out.println("Invalid choice. Running hardcoded examples.");
            executeHardcoded();
        }
        
        scanner.close();
    }

    // Hardcoded Execution
    public static void executeHardcoded() {
        System.out.println("\n=== RUNNING WITH HARCODED EXAMPLES ===\n");
        
        System.out.println("1. Top K Frequent Words:");
        System.out.println("Result: " + topKFrequent(
            new String[]{"java", "python", "java", "golang", "java", "python"}, 2));
        
        System.out.println("\n2. JSON Validator:");
        System.out.println("Is valid? " + isValidJson(Map.of("name", "Test", "value", 123)));
        
        System.out.println("\n3. Mahasiswa Class:");
        Mahasiswa mhs = new Mahasiswa("Budi Santoso", "20230001", 85.5);
        System.out.println(mhs);
        System.out.println("Lulus? " + mhs.isLulus());
        
        System.out.println("\n4. Word Counter:");
        System.out.println("Result: " + countWords("This is a test. This is only a test"));
    }
    
    // Manual Input Execution
    public static void executeManual(Scanner scanner) {
        System.out.println("\n=== MANUAL INPUT MODE ===\n");
        
        // 1. Top K Frequent Words
        System.out.println("1. TOP K FREQUENT WORDS");
        System.out.print("Enter words (comma separated): ");
        String wordsInput = scanner.nextLine();
        String[] words = Arrays.stream(wordsInput.split(","))
                .map(String::trim)
                .toArray(String[]::new);
        System.out.print("Enter k value: ");
        int k = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Result: " + topKFrequent(words, k));
        
        // 2. JSON Validator - Manual JSON creation
        System.out.println("\n2. JSON VALIDATOR");
        Map<String, Object> json = createJsonManually(scanner);
        System.out.println("Created JSON: " + json);
        System.out.println("Is valid? " + isValidJson(json));
        
        // 3. Mahasiswa
        System.out.println("\n3. MAHASISWA CLASS");
        System.out.print("Enter student name: ");
        String nama = scanner.nextLine();
        System.out.print("Enter student NIM: ");
        String nim = scanner.nextLine();
        System.out.print("Enter student score (0-100): ");
        double nilai = scanner.nextDouble();
        scanner.nextLine();
        
        try {
            Mahasiswa mahasiswa = new Mahasiswa(nama, nim, nilai);
            System.out.println(mahasiswa);
            System.out.println("Lulus? " + mahasiswa.isLulus());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        
        // 4. Word Counter
        System.out.println("\n4. WORD COUNTER");
        System.out.print("Enter text to analyze: ");
        String text = scanner.nextLine();
        System.out.println("Word count: " + countWords(text));
    }
    
    private static Map<String, Object> createJsonManually(Scanner scanner) {
        Map<String, Object> json = new HashMap<>();
        
        System.out.print("How many JSON key-value pairs? ");
        int pairs = scanner.nextInt();
        scanner.nextLine();
        
        for (int i = 0; i < pairs; i++) {
            System.out.print("Enter key " + (i+1) + ": ");
            String key = scanner.nextLine();
            
            System.out.print("Enter type for value (string/number/boolean/list/map): ");
            String type = scanner.nextLine().toLowerCase();
            
            Object value = null;
            switch (type) {
                case "string":
                    System.out.print("Enter string value: ");
                    value = scanner.nextLine();
                    break;
                case "number":
                    System.out.print("Enter number value: ");
                    value = scanner.nextDouble();
                    scanner.nextLine();
                    break;
                case "boolean":
                    System.out.print("Enter boolean value (true/false): ");
                    value = scanner.nextBoolean();
                    scanner.nextLine();
                    break;
                case "list":
                    System.out.print("Enter list items (comma separated): ");
                    String[] items = scanner.nextLine().split(",");
                    value = Arrays.asList(items);
                    break;
                case "map":
                    value = createJsonManually(scanner);
                    break;
                default:
                    value = "invalid";
            }
            
            json.put(key, value);
        }
        
        return json;
    }

    // 1. Top K Frequent Words Method
    public static List<String> topKFrequent(String[] words, int k) {
        return Arrays.stream(words)
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet().stream()
                .sorted(Comparator
                    .comparingLong(Map.Entry<String, Long>::getValue).reversed()
                    .thenComparing(Map.Entry::getKey))
                .limit(k)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    // 2. JSON Validator Method
    public static boolean isValidJson(Object obj) {
        if (obj == null) {
            return true;
        }
        
        if (obj instanceof String || obj instanceof Number || obj instanceof Boolean) {
            return true;
        }
        
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            return map.entrySet().stream()
                    .allMatch(e -> e.getKey() instanceof String && isValidJson(e.getValue()));
        }
        
        if (obj instanceof List) {
            List<?> list = (List<?>) obj;
            return list.stream().allMatch(SigmatechBackend::isValidJson);
        }
        
        if (obj instanceof Object[]) {
            Object[] arr = (Object[]) obj;
            return Arrays.stream(arr).allMatch(SigmatechBackend::isValidJson);
        }
        
        return false;
    }
    
    // 3. Mahasiswa Method
    public static class Mahasiswa {
        private final String nama;
        private final String nim;
        private final double nilai;
        
        public Mahasiswa(String nama, String nim, double nilai) {
            this.nama = Objects.requireNonNull(nama, "Nama tidak boleh null");
            this.nim = Objects.requireNonNull(nim, "NIM tidak boleh null");
            
            if (nama.isBlank() || nim.isBlank()) {
                throw new IllegalArgumentException("Nama dan NIM tidak boleh kosong");
            }
            if (nilai < 0 || nilai > 100) {
                throw new IllegalArgumentException("Nilai harus antara 0-100");
            }
            
            this.nilai = nilai;
        }
        
        public boolean isLulus() { 
            return nilai > 70; 
        }
        
        public String getNama() { return nama; }
        public String getNim() { return nim; }
        public double getNilai() { return nilai; }
        
        @Override
        public String toString() {
            return String.format("Mahasiswa[nama=%s, nim=%s, nilai=%.1f, status=%s]", 
                nama, nim, nilai, isLulus() ? "LULUS" : "TIDAK LULUS");
        }
    }
    
    // 4. Word Counter Method
    public static Map<String, Long> countWords(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyMap();
        }
        
        return Arrays.stream(text.split("\\s+"))
                .filter(w -> !w.isBlank())
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
    }
}