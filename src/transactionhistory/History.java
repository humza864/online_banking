
package transactionhistory;

public class History {
        private String account;
        private String amount;
        private String generic;
        private String date;
        private String time;
        private String tax;

    public History(String account, String amount, String generic, String tax, String date, String time) {
        this.account = account;
        this.amount = amount;
        this.generic = generic;
        this.date = date;
        this.time = time;
        this.tax = tax;
    }

    public History(String account, String amount, String generic, String date, String time) {
        this.account = account;
        this.amount = amount;
        this.generic = generic;
        this.date = date;
        this.time = time;
    }

    public String getAccount() {
        return account;
    }

    public String getAmount() {
        return amount;
    }

    public String getGeneric() {
        return generic;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
    public String getTax(){
        return tax;
    }
        
    
}
