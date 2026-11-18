package productapp;

public class ProductCalculations implements IProductCalculations {

    private double price;

    public ProductCalculations(double price) {
        this.price = price;
    }

    @Override
    public double GetVat() {
        return price * 15 / 100;
    }

    @Override
    public double GetCommission() {
        return price * 5 / 100;
    }

    @Override
    public double GetTotal() {
        return price + GetVat() + GetCommission();
    }
}
