package application.models.sushi;

public class Sushi {
	String SushiId, Name, Description;
	int Price, Stock;
	
	public Sushi(String SushiId, String Name, String Description, int Price, int Stock) {
		this.SushiId = SushiId;
		this.Name = Name;
		this.Description = Description;
		this.Price = Price;
		this.Stock = Stock; 
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
	
	public String  getDescription() {
		return Description;
	}
	
	public void setDescription(String Description) {
		this.Description = Description;
	}
	
	public int getPrice() {
		return Price;
	}
	
	public void setPrice(int Price) {
		this.Price = Price;
	}
	
	public int getStock() {
		return Stock;
	}
	
	public void setStock(int Stock) {
		this.Stock = Stock;
	}
}
