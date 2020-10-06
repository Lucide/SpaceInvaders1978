

public class Car{
	private final Builder attributes;

	private Car(Builder attributes){
		this.attributes=attributes;
	}

	public static class Builder{
		private static Builder builderAttributes=new Builder();
		private String color, size, speed;

		public Builder(){
			color="default color";
			size="default size";
			speed="default speed";
		}

		private static void reset(){
			builderAttributes=new Builder();
		}

		public static void addColor(String color){
			builderAttributes.color=color;
		}

		public static void addSize(String size){
			builderAttributes.size=size;
		}

		public static void addSpeed(String speed){
			builderAttributes.speed=speed;
		}

		public static Car build(){
			Builder builtAttributes=builderAttributes;
			reset();
			return new Car(builtAttributes);
		}
	}

	@Override
	public String toString(){
		return "Car [color="+attributes.color+", size="+attributes.size+", speed="+attributes.speed+"]";
	}
}
