import java.util.*;

public class MatrixArr2D {

	public static void main(String[] args) {
		int size = 4;
		MatrixArr2D matrix2D = new MatrixArr2D();
		ArrayList<ArrayList<Integer>> matrix = matrix2D.buildMatrix(size);
		matrix2D.printMatrix(matrix);
		System.out.println("");
		ArrayList<ArrayList<Integer>> rotatedMatrix = matrix2D.rotateMatrix(matrix, size);
		matrix2D.printMatrix(rotatedMatrix);
	}

	public ArrayList<ArrayList<Integer>> buildMatrix(int size) {
		ArrayList<ArrayList<Integer>> outerMatrix = new ArrayList<>();
		for (int i=0; i<size;i++) {
			ArrayList<Integer> innerMatrix = new ArrayList<>();
			Random rand = new Random();
			for (int j=0; j<size;j++) {
				innerMatrix.add(rand.nextInt(10, 100));
			}
			outerMatrix.add(innerMatrix);
		}
		return outerMatrix;
	}

	public ArrayList<ArrayList<Integer>>  rotateMatrix( ArrayList<ArrayList<Integer>> matrix, int size) {
		for (int i=0; i<size; i++ ) {
			for (int j=i+1;j<size;j++) {
				int temp = matrix.get(i).get(j);
				matrix.get(i).set(j, matrix.get(j).get(i));
				matrix.get(j).set(i, temp);
			}
		}
		for (int i=0; i<size; i++ ) {
			ArrayList<Integer> row = matrix.get(i);
			for (int j=0; j< size/2; j++) {
				int temp = row.get(j);
				row.set(j,row.get(size-1-j));
				row.set(size-1-j, temp);
			}
		}
		return matrix;
	}

	public ArrayList<ArrayList<Integer>> buildEmptyMatrix(int size) {
		ArrayList<ArrayList<Integer>> outerMatrix = new ArrayList<>();
		for (int i=0; i<size;i++) {
			ArrayList<Integer> innerMatrix = new ArrayList<>();
			for (int j=0; j<size;j++) {
				innerMatrix.add(0);
			}
			outerMatrix.add(innerMatrix);
		}
		return outerMatrix;
	}

	public void printMatrix( ArrayList<ArrayList<Integer>> matrix) {
		matrix.stream().forEach(oItem -> { 
			System.out.println("");
			oItem.stream().forEach(item -> {
				System.out.print(item + " ");
			});
		});
	}

	public void printRow(ArrayList<Integer> row) {
		row.stream().forEach(item -> { 
			System.out.print(item + " ");
		});
		System.out.println("");
	}
}
