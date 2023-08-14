
class UserIdsGenerator {
    private int id = 1;
    static private UserIdsGenerator self;
    private UserIdsGenerator() {
    }

    public static UserIdsGenerator getInstance() {
        if (self == null) {
            self = new UserIdsGenerator();
        }
        return self;
    }

    public int generateId() {
        return this.id++;
    }

}