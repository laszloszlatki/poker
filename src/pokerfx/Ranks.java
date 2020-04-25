/*
 * enum to hold all available ranks
 * in order of strength
 * */

package pokerfx;

/*
 * @author: Laszlo Szlatki
 * @date: 10/Mar/2020
 * */

// list of all possible ranks
// value added to ranks for further reuse in different card game
enum Ranks {
	TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10),
	ACE(11);

	final int value;

	Ranks(int value) {
		this.value = value;
	}
}
