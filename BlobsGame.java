
import java.util.List;


public class BlobsGame implements Game<BlobsState, XYLocation, String>{
	BlobsState initialState = new BlobsState();
	int i = 0;
	@Override
	public List<XYLocation> getActions(BlobsState state) {
		// TODO Auto-generated method stub
		return state.getUnMarkedPositions();
	}

	@Override
	public BlobsState getInitialState() {
		// TODO Auto-generated method stub
		return initialState;
	}

	@Override
	public String getPlayer(BlobsState state) {
		// TODO Auto-generated method stub 
		return state.getPlayerToMove();
		
	}

	@Override
	public String[] getPlayers() {
		// TODO Auto-generated method stub
		return new String[] { BlobsState.X, BlobsState.O };
	}

	@Override
	public BlobsState getResult(BlobsState state, XYLocation action) {
		// TODO Auto-generated method stub

		BlobsState result = state.clone();
		result.mark(action);
		return result;
	}

	public double getUtility(BlobsState state, String player) {
		// TODO Auto-generated method stub
		double result = state.getUtility();
		
		if (result != -1) {
			if (player == BlobsState.O)
				result = 1 - result;
		} else {
			throw new IllegalArgumentException("State is not terminal.");
		}
		return result;
	}
	
	@Override
	public boolean isTerminal(BlobsState state) {
		// TODO Auto-generated method stub
		return state.getUtility() != -1;
	}

	@Override
	public boolean isCutOff(BlobsState state) {
		// TODO Auto-generated method stub
		if(i == 40){
			return true;
		}
		i++;
		return false;
	}

	@Override
	//EVALUATION FUNCTION
	public double getEval(BlobsState state, String player) {
		// TODO Auto-generated method stub
		double result = state.getUtility();
		if (result != -1) {
			if (player == BlobsState.O)
				result = state.oCount;
			else
				result = state.xCount;
		}
		return result;
	}
}
