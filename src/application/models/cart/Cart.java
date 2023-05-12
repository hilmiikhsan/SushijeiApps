package application.models.cart;

public class Cart {
	String SushiId, Name;
	int Price, Quantity;
	
	public Cart(String SushiId, String Name, int Price, int Quantity) {
		this.SushiId = SushiId;
		this.Name = Name;
		this.Price = Price;
		this.Quantity = Quantity;
	}
	
	public String getSushiId() { 
		return SushiId;
	}
	
	public void setSushiId(String SushiId) {
		this.SushiId = SushiId;
	}
	
	public String getName() {
		return Name;
	}
	
	public void setName(String Name) {
		this.Name = Name;
	}
	
	public int getPrice() {
		return Price;
	}
	
	public void setPrice(int Price) {
		this.Price = Price;
	}
	
	public int getQuantity() {
		return Quantity;
	}
	
	public void setQuantity(int Quantity) {
		this.Quantity = Quantity;
	}
}
