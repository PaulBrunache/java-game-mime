import java.util.Random;

import aima.core.search.adversarial.AdversarialSearch;


public class BlobsDemo {
	public static void main(String[] args) {
		System.out.println("BLOBS DEMO");
		System.out.println("");
		startMinimaxDemo();
	}

	private static void startMinimaxDemo() {
		int count = 0;

		while (count != 2){
			System.out.println("MINI MAX DEMO WITH TWO RANDOM vs MiniMax with cutoff done");
			BlobsGame game = new BlobsGame();
			BlobsState currState = game.getInitialState();
			System.out.println(currState);
		
			AdversarialSearch<BlobsState, XYLocation> search = MinimaxSearch
					.createFor(game);
			while (!(game.isTerminal(currState))) {
				System.out.println(game.getPlayer(currState) + "  playing ... ");
				if(game.getPlayer(currState) == "O"){
					System.out.println("Random Agent Playing...");
					Random rand = new Random(System.currentTimeMillis());
					int num = rand.nextInt(game.getActions(currState).size());
					XYLocation action = game.getActions(currState).get(num);
					currState = game.getResult(currState, action);			}
				else {
					System.out.println("MiniMax with cutOff Agent Playing...");
					XYLocation action = search.makeDecision(currState);
					currState = game.getResult(currState, action);
				}
				System.out.println(currState);
			}
			currState.printWinner();
			System.out.println("MINI MAX DEMO WITH TWO RANDOM vs MiniMax with cutoff done");
			System.out.println("----------------------------------------------------------");
			
			System.out.println("MINI MAX DEMO WITH TWO RANDOM AGENTS\n");
			game = new BlobsGame();
			currState = game.getInitialState();
			System.out.println(currState);
			while (!(game.isTerminal(currState))) {
				System.out.println(game.getPlayer(currState) + "  playing ... " );
				Random rand = new Random(System.currentTimeMillis());
				int num = rand.nextInt(game.getActions(currState).size());
				XYLocation action = game.getActions(currState).get(num);
				currState = game.getResult(currState, action);
				System.out.println(currState);
			}
			currState.printWinner();
			System.out.println("MINI MAX DEMO WITH TWO RANDOM AGENTS done");
			count ++;
		}
	}
}
