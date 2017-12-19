
public class Namelist {
    private String namelist[];
    private static final int MAX = 6;
    public Namelist() {
    	namelist = new String[MAX];
		namelist[0] = "Lennart Monir";
		namelist[1] = "Marvin Hoff";
		namelist[2] = "Mathias Wittling";
		namelist[3] = "Janek Dahl";
		namelist[4] = "Maritza Villa";
		namelist[5] = "Cheik DaBoss";
	}

	public String getName(String s) {
		String[] names = s.split(";");
		int l = names.length;
		String name = "";
		for (int i = 0; i < l; i++) {
			try {
			name = name + findName(names[i]);
			}catch (Exception e) {
				name = name + e + "\n";
			}
		}
		return name;
	}

	private String findName(String s) throws NoNameFoundException {
		String name = "";

		for (int j = 0; j < MAX; j++) {			
				if (namelist[j].contains(s)) {
					name = name + namelist[j] + " " + "\n";
				}			
		}
		if (name.equals("")) {
			throw new NoNameFoundException("Der Name " + s + " ist nicht vorhanden!");
		}
		return name;
	}
}


