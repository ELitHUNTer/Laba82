package Organizations;

/**
 * типы организации
 */
public enum OrganizationType {
    COMMERCIAL,
    TRUST,
    PRIVATE_LIMITED_COMPANY,
    OPEN_JOINT_STOCK_COMPANY;

    public static int getPosition(String s){
        if (String.valueOf(COMMERCIAL).equals(s))
            return 0;
        else if (String.valueOf(TRUST).equals(s))
            return 1;
        else if (String.valueOf(PRIVATE_LIMITED_COMPANY).equals(s))
            return 2;
        else if (String.valueOf(OPEN_JOINT_STOCK_COMPANY).equals(s))
            return 3;
        else
            return 4;
    }

    public static String getValue(int d){
        switch (d){
            case 0:
                return "COMMERCIAL";
            case 1:
                return "TRUST";
            case 2:
                return "PRIVATE_LIMITED_COMPANY";
            case 3:
                return "OPEN_JOINT_STOCK_COMPANY";
            case 4:
                return "NONE";
            default:
                return "";
        }
    }
}
