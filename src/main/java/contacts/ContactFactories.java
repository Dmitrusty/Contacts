package contacts;/*interface AbstractFactory {
    Contact makeContact();
}*/

class PersonsFactory {
    // @Override
    public static Contact makeContact() {

        String name = Person.enterName();
        String surname = Person.enterSurname();
        String birth = Person.enterBirth();
        String gender = Person.enterGender();
        String phone = Person.enterPhone();

        return new Person(name, surname, birth, gender, phone);
    }
}

class FirmsFactory{
    // @Override
    public static Contact makeContact() {

        String name = Firm.enterName();
        String address = Firm.enterAddress();
        String phone = Firm.enterPhone();

        return new Firm(name, address, phone);
    }
}

