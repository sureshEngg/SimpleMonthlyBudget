package suresh.expensesimpletracking;

public class memberType {
	private int id;
	String name;

	public memberType(int _id, String _name) {
		// TODO Auto-generated constructor stub
		setId(_id);
		name = _name;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return name;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
}