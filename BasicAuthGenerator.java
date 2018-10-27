import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.Base64;

public class BasicAuthGenerator {

    private static final String clientId = "MsOIJ39Q28";
    private static final String clientSecret = "PTDc3H8a)Vi=UYap";

    public static void main (String[] args){
        System.out.println("Basic auth is: "+getBasicAuth(getInstallationID()));
    }

    //you make up any android id or mac address. These are just examples
    private static String generateID() {
        String myid = "9f510484b20a905a"; //some random androidid
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("androidId ");
        stringBuilder.append(myid);
        stringBuilder = new StringBuilder();
        stringBuilder.append("WF:3H:6F:3C:2F:5M"); //some random WIFI mac address
        String str = stringBuilder.toString();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("macAddr ");
        stringBuilder2.append(str);
        myid = new UUID((long) myid.hashCode(), (long) str.hashCode()).toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append("deviceId ");
        stringBuilder.append(myid); // device id made using android id and wifi mac address
        return myid;
    }

    private static String getInstallationID() {
        Throwable e;
        System.out.println("Creating installation ID...");
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("android_1_");
        stringBuilder.append(currentTimeMillis);
        stringBuilder.append("_");
        stringBuilder.append(generateID());
        String stringBuilder2 = stringBuilder.toString();
        stringBuilder = new StringBuilder();
        stringBuilder.append(" input ");
        stringBuilder.append(stringBuilder2);
        try {
            stringBuilder2 = hashit(stringBuilder2);
            try {
                stringBuilder = new StringBuilder();
                stringBuilder.append(" output ");
                stringBuilder.append(stringBuilder2);
            } catch (Exception e2) {
                e = e2;
                System.out.println("Failed to create installation ID");
                return stringBuilder2;
            }
        } catch (Exception e3) {
            e = e3;
            stringBuilder2 = null;
            System.out.println("Failed to create installation ID");
            return stringBuilder2;
        }
        return stringBuilder2;
    }

    public static String hashit(String str) throws NoSuchAlgorithmException{
        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        instance.reset();
        return tohex(instance.digest(str.getBytes(StandardCharsets.UTF_8)));
    }

    public static String tohex(byte[] bArr) {
        StringBuilder stringBuilder = new StringBuilder();
        int length = bArr.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(String.format("%02X", bArr[i]));
        }
        return stringBuilder.toString();
    }


    private static String getBasicAuth(String str) {
        try {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append('_');
            stringBuilder.append(clientId);
            stringBuilder.append(':');
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(":");
            stringBuilder2.append(clientId);
            stringBuilder2.append(":");
            stringBuilder2.append(clientSecret);
            stringBuilder.append(tokenhash(stringBuilder2.toString()).toLowerCase());
            String encodeToString = Base64.getEncoder().encodeToString(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Basic ");
            stringBuilder2.append(encodeToString);
            return stringBuilder2.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }

    }

    public static String tokenhash(String str) throws NoSuchAlgorithmException {
        MessageDigest instance = MessageDigest.getInstance("SHA-1");
        byte[] bytes = str.getBytes(StandardCharsets.UTF_8);
        instance.reset();
        instance.update(bytes, 0, bytes.length);
        return tohex(instance.digest());
    }


}
