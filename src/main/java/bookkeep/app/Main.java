package bookkeep.app;

public class Main {

	public static void main(String[] args) {
		System.out.print("\033[2J\033[1;1H");
		String string = "HEI på deg 123";

		string = string.substring(0, string.length() - 2);
		System.out.println(string);

	}
}
