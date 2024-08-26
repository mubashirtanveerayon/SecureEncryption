package encryption2;

import constants.Constants;
import utils.Random;
import utils.UnInitializedException;
import utils.UnexpectedDigitException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

public class SecureEncryption {


    private Random rng;


    private int[] shiftKey;
    private int[] multKey;
    private int keyPK;


    private HashMap<String,Integer> properties;


    private HashMap<Integer,Character> alias;


    private final char negative;


    private String validReplacer;



    public SecureEncryption(long seed){
        rng = new Random(seed);
        properties = new HashMap<>();
        properties.put(Constants.MIN_KEY_LENGTH_PROPERTY,Constants.DEFAULT_MIN_KEY_LENGTH);
        properties.put(Constants.MAX_KEY_LENGTH_PROPERTY,Constants.DEFAULT_MAX_KEY_LENGTH);
        properties.put(Constants.SHIFT_RANGE_PROPERTY,Constants.DEFAULT_SHIFT_RANGE);
        properties.put(Constants.MAX_KEY_PK_RANGE_PROPERTY,Constants.DEFAULT_MAX_KEY_PK_RANGE);
        properties.put(Constants.MIN_KEY_PK_RANGE_PROPERTY,Constants.DEFAULT_MIN_KEY_PK_RANGE);
        properties.put(Constants.MULT_RANGE_PROPERTY,Constants.DEFAULT_MULT_RANGE);

        negative = Constants.SPECIAL_CHARACTER.charAt(rng.nextInt(Constants.SPECIAL_CHARACTER.length()));

        validReplacer = Constants.DIGIT_REPLACER;// + Constants.SPECIAL_CHARACTER.replace(Character.toString(negative),"");

    }

    public void setProperties(HashMap<String , Integer>encryptionProperties){
        properties.put(Constants.MIN_KEY_LENGTH_PROPERTY,encryptionProperties.get(Constants.MIN_KEY_LENGTH_PROPERTY));
        properties.put(Constants.MAX_KEY_LENGTH_PROPERTY,encryptionProperties.get(Constants.MAX_KEY_LENGTH_PROPERTY));
        properties.put(Constants.SHIFT_RANGE_PROPERTY,encryptionProperties.get(Constants.SHIFT_RANGE_PROPERTY));
        properties.put(Constants.MAX_KEY_PK_RANGE_PROPERTY,encryptionProperties.get(Constants.MAX_KEY_PK_RANGE_PROPERTY));
        properties.put(Constants.MIN_KEY_PK_RANGE_PROPERTY,encryptionProperties.get(Constants.MIN_KEY_PK_RANGE_PROPERTY));
        properties.put(Constants.MULT_RANGE_PROPERTY,encryptionProperties.get(Constants.MULT_RANGE_PROPERTY));
    }


    public void initialize(){

        int keyLength = rng.nextInt(properties.get(Constants.MIN_KEY_LENGTH_PROPERTY),properties.get(Constants.MAX_KEY_LENGTH_PROPERTY)+1);
        shiftKey = new int[keyLength];
        multKey = new int[keyLength];
        keyPK = rng.nextInt(properties.get(Constants.MIN_KEY_PK_RANGE_PROPERTY),properties.get(Constants.MAX_KEY_PK_RANGE_PROPERTY)+1);
        int shiftKeyRange = properties.get(Constants.SHIFT_RANGE_PROPERTY);
        int multKeyRange = properties.get(Constants.MULT_RANGE_PROPERTY);
        for(int i=0;i<keyLength;i++){
            shiftKey[i] = rng.nextInt(-shiftKeyRange,shiftKeyRange+1);
            multKey[i] = rng.nextInt(2,multKeyRange+1);
        }

        alias = new HashMap<>();


        for(int i=0;i<10;i++){
            char replaced = validReplacer.charAt(rng.nextInt(validReplacer.length()));
            validReplacer=validReplacer.replace(Character.toString(replaced),"");
            alias.put(i,replaced);
        }

    }


    public int[] encrypt(byte[] plainData){
        if(shiftKey == null || multKey == null || alias == null || alias.isEmpty())throw new UnInitializedException("SecureEncryption object was not initialized");
        Base64.Encoder b64Encoder = Base64.getEncoder();
        byte[] pb64 = b64Encoder.encode(plainData);



        int publicKey = 0;

        for(byte b:pb64)publicKey += b;

//        publicKey /= pb64.length;
        int[] e1 = new int[plainData.length];

        int dataPadding = 1;

        int[] encrypted = new int[dataPadding+e1.length*3];


        encrypted[dataPadding-1] = publicKey;

        int keyIndex=0;
        
        int modKey = publicKey + keyPK;
        for(int i=0;i<plainData.length;i++){
            e1[i] = (plainData[i] + shiftKey[keyIndex]) * multKey[keyIndex];

            int sign=1;
            if(e1[i] < 0){
                sign=-1;
            }

            e1[i] *= sign;



            encrypted[dataPadding+i]=sign;
            encrypted[dataPadding+i+e1.length]=(e1[i] / modKey);
            encrypted[dataPadding+i+e1.length*2] = (e1[i] % modKey);

            keyIndex++;
            if(keyIndex == shiftKey.length)keyIndex=0;
        }




        return encrypted;
    }

    public byte[] decrypt(int[] array){


        int publicKey = array[0];
        int modKey = publicKey + keyPK;
        byte[] plainData = new byte[(array.length-1)/3];
        int keyIndex=0;
        for(int i=0;i< plainData.length;i++){


            int sign = array[1+i];
            int q = array[1+plainData.length+i];
            int r = array[1+plainData.length*2+i];
            int e1 = (modKey * q + r) * sign;

            plainData[i] = (byte)(e1/multKey[keyIndex]-shiftKey[keyIndex]);

            keyIndex++;
            if(keyIndex == shiftKey.length)keyIndex=0;
        }

        return plainData;
    }

    public String encrypt(String text){
        return arrayToString(encrypt(text.getBytes()));
    }

    public String decrypt(String encrypted){
        return new String(decrypt(stringToArray(encrypted)),StandardCharsets.UTF_8);
    }

    public String arrayToString(int[] array){

        StringBuilder sb=new StringBuilder();

        for(int i=0;i<array.length;i++){
            int value = array[i];
            String stringValue = Integer.toString(value);

            if(value<0){
                sb.append(negative);
                stringValue=stringValue.replace("-","");
            }
            for(char c:stringValue.toCharArray())sb.append(alias.get(((int)c)-48));

            if(i<array.length-1) sb.append(Constants.SEPARATOR);


        }

        return sb.toString();

    }

    public int[] stringToArray(String string){

        String[] elements = string.split(Character.toString(Constants.SEPARATOR));

        int[] array = new int[elements.length];

        for(int i=0;i<elements.length;i++){
            int value=0;
            int sign=1;

            for(int j=0;j<elements[i].length();j++){
                char c = elements[i].charAt(j);
                if(c == negative)sign=-1;
                else{
                    int digit=-1;

                    for(Integer key:alias.keySet()) {
                        if (alias.get(key) == c) {
                            digit = key;
                            break;
                        }
                    }

                    if(digit<0)throw new UnexpectedDigitException(c+" is not an alias");
                    value = 10 * value + digit;
                }
            }

            array[i] = value * sign;

        }


        return array;
    }

//    public int hash(byte[] data){
//        int hash=0;
//
//        int[] encryptedArray = encrypt(data);
//
//        for(int cipher:encryptedArray)hash ^= cipher;
//
//
//
//        return hash;
//    }


}
