package Organizations;

import android.util.Log;

import java.time.LocalDate;
import java.util.Comparator;

/**
 * Класс содержащий основную информацию об организации
 */
public class Organization implements Comparable {
    private static long counter = 0;
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int annualTurnover; //Значение поля должно быть больше 0
    private String fullName; //Значение этого поля должно быть уникальным, Поле не может быть null
    private OrganizationType type; //Поле может быть null
    private Address officialAddress; //Поле может быть null
    private String owner;

    public Organization(String name, String fullName, Long x, long y, int annualTurnover, String organizationType, String street, String zipCode, Long location_x, Long location_y, String locationName, String owner) throws Exception {
        if (name == null || name.equals(""))
            throw new Exception("Строка не может быть пустой");
        if (annualTurnover <= 0)
            throw new Exception("annualTurnover должен быть больше 0");
        if (organizationType == null)
            type = null;
        else
            type = OrganizationType.valueOf(organizationType);
        coordinates = new Coordinates(x, y);
        id = ++counter;
        this.fullName = fullName;
        this.name = name;
        this.annualTurnover = annualTurnover;
        //creationDate = LocalDate.now();
        officialAddress = new Address(street, zipCode, location_x, location_y, locationName);
        this.owner = owner;
    }

    /**
     * Заменить организацию на предоставленную
     * @param organization предоставленная организация
     */
    public void update(Organization organization) {
        if (organization == null) {
            return;
        }
        name = organization.name;
        coordinates = organization.coordinates;
        creationDate = organization.creationDate;
        annualTurnover = organization.annualTurnover;
        fullName = organization.fullName;
        type = organization.type;
        officialAddress = organization.officialAddress;
    }

    /**
     *
     * @return Возвращает строковое представление данной организации
     */
    @Override
    public String toString() {
        return "Organization{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", annualTurnover=" + annualTurnover +
                ", fullName='" + fullName + '\'' +
                ", type=" + type +
                ", officialAddress=" + officialAddress +
                ", owner=" + owner +
                '}';
    }

    /**
     *
     * @return Строковое представление данной организации в формате csv
     */
    public String toCSV(){
        return name+','+fullName+','+coordinates.toCSV()+','+annualTurnover+','+type+','+officialAddress.toCSV()+','+owner;
    }

    /**
     *
     * @return id данной организации
     */
    public Long getID(){
        return id;
    }

    /**
     *
     * @return Возвращает название данной организации
     */
    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getX(){
        return coordinates.getX();
    }

    public long getY(){
        return coordinates.getY();
    }

    /**
     *
     * @return AnnualTurnover данной организации
     */
    public int getAnnualTurnover(){
        return annualTurnover;
    }

    /**
     *
     * @return Возвращает тип данной организации
     */
    public OrganizationType getType() {
        return type;
    }

    public String getStreet(){
        return officialAddress.getStreet();
    }

    public String getZipCode(){
        return officialAddress.getZipCode();
    }

    public Long getLocationX(){
        return officialAddress.getLocationX();
    }

    public Long getLocationY(){
        return officialAddress.getLocationY();
    }

    public String getLocationName(){
        return officialAddress.getLocationName();
    }

    public String getOwner(){
        return owner;
    }

    /**
     * Сравнивает 2 организации
     * @param o организация, с которой сравнивает
     * @return результат сравнения
     */
    @Override
    public int compareTo(Object o) {
        return annualTurnover - ((Organization)o).annualTurnover;
    }

    public static void resetCounter(){
        counter = 0;
    }

    public static class OrgComparator implements Comparator<Organization>{

        private boolean[] needSort;
        private static int UNSORTED = Integer.MIN_VALUE;

        public OrgComparator (boolean[] needSort) {
            this.needSort = needSort;
        }

        @Override
        public int compare(Organization o1, Organization o2) {
            int c = UNSORTED;

            if (needSort[0] && c == UNSORTED)
                c = (int) (o1.id - o2.id);
            if (needSort[1] && c == UNSORTED)
                c = o1.name.compareTo(o2.name);
            if (needSort[2] && c == UNSORTED)
                c = o1.fullName.compareTo(o2.fullName);
            if (needSort[3] && c == UNSORTED)
                c = (int) (o1.getX() - o2.getX());
            if (needSort[4] && c == UNSORTED)
                c = (int) (o1.getY() - o2.getY());
            if (needSort[5] && c == UNSORTED)
                c = o1.getAnnualTurnover() - o2.getAnnualTurnover();
            if (needSort[6] && c == UNSORTED)
                c = String.valueOf(o1.getType()).compareTo(String.valueOf(o2.type));
            if (needSort[7] && c == UNSORTED)
                c = o1.getStreet().compareTo(o2.getStreet());
            if (needSort[8] && c == UNSORTED)
                c = o1.getZipCode().compareTo(o2.getZipCode());
            if (needSort[9] && c == UNSORTED)
                c = (int) (o1.getLocationX() - o2.getLocationX());
            if (needSort[10] && c == UNSORTED)
                c = (int) (o1.getLocationY() - o2.getLocationY());
            if (needSort[11] && c == UNSORTED)
                c = o1.getLocationName().compareTo(o2.getLocationName());
            if (needSort[12] && c == UNSORTED)
                c = o1.getOwner().compareTo(o2.getOwner());

            Log.e("123", String.valueOf(c));

            if (c == UNSORTED)
                c = (int) (o1.id - o2.id);

            return c;
        }
    }
}
