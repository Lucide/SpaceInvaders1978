

public class Main{
	public static void main(String[] args){
		Car.Builder.addColor("red");
		Car.Builder.addSize("big");
		Car.Builder.addSpeed("fast");
		Car car1=Car.Builder.build();

		Car.Builder.addColor("blue");
		Car.Builder.addSize("small");
		Car.Builder.addSpeed("slow");
		Car car2=Car.Builder.build();

		System.out.println(car1.toString());
		System.out.println(car2.toString());
	}

}
