package bofjo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.GregorianCalendar;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

public class AppGUI extends Application{
	private File monthData = new File(GregorianCalendar.DAY_OF_MONTH + "_" + new GregorianCalendar().getTime().toString().substring(26) + "_data.dat");
	private File userData = new File("user_data.txt");
	private double budget;
	private String name;
	private MoneyDataList values;
	private Font font = new Font("Agency FB", 25), fontB = new Font("Agency FB Bold", 31), font2 = new Font("Agency FB", 27), font2b = new Font("Agency FB Bold", 40);
	private int width = 960, height = 600;
	
	/* TO-DO: 	
	 * Switch to Try Catch for initial boot
	 */
	
	public void start(Stage primaryStage) {
		//try to read data, if it doesnt exist, create the file 
		
		try{
			initializeVars();
			Scene dataScreen = new Scene(getDataScreen(primaryStage), width, height);
			primaryStage.setTitle("Main Menu");
			primaryStage.setScene(dataScreen);
		} catch (Exception ex) {
			Scene splashScreen = new Scene(getSplashScreen(primaryStage), 265, 100);
			primaryStage.setTitle("First Time Start-Up");
			primaryStage.setScene(splashScreen);
		} finally {primaryStage.show();
		}
	/*	if(!userData.exists()) {
			Scene splashScreen = new Scene(getSplashScreen(primaryStage), 265, 100);
			primaryStage.setTitle("User Settings");
			primaryStage.setScene(splashScreen);
		}
		else {
			initializeVars();
			for(int i = 0; i < values.size(); i++)
				System.out.println(values.get(i));
			Scene dataScreen = new Scene(getDataScreen(primaryStage), width, height);
			primaryStage.setTitle("Main Menu");
			primaryStage.setScene(dataScreen);
		}
		primaryStage.show();
	 */
	}
	
		
	protected void initializeVars() throws Exception{
		//name and budget
		
		try (DataInputStream input = new DataInputStream(new FileInputStream(userData));){
			char c;
			while((c = input.readChar()) != '\n') {
				name += c;
			}
			name = name.substring(4);
			budget = input.readDouble();
		}catch (IOException ex) {
			System.out.println("Error thrown in initializeVars");
			throw new Exception();
		}
		
		//moneylist values
			
		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(monthData));){	
			values = (MoneyDataList)input.readObject();
			input.close();
		}catch (IOException ex) {
			System.out.println("No data file found");
			
			try(FileOutputStream output = new FileOutputStream(monthData);){
				values = new MoneyDataList();
				output.close();
			}catch(IOException ex2) {}
			
		}catch (ClassNotFoundException ex2) {}
	}
	
	protected BorderPane getSplashScreen(Stage primaryStage) {		
		//Font not standardized
		BorderPane splashBorder = new BorderPane();
		GridPane splashGrid = new GridPane();
		splashGrid.setPadding(new Insets(5));
		
		TextField nameField = new TextField();
		TextField budgetField = new TextField();
		nameField.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE,null,null)));
		budgetField.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE,null,null)));
		nameField.setPromptText("ex. Paul Fodor");
		budgetField.setPromptText("ex. 100.00");
		
		splashGrid.add(new Label("Name: "), 0, 0);
		splashGrid.add(nameField, 1, 0);
		splashGrid.add(new Label("Monthly Budget: "), 0, 1);
		splashGrid.add(budgetField, 1, 1);
		splashGrid.setVgap(10);
		splashGrid.setHgap(10);
		splashBorder.setCenter(splashGrid);
		
		VBox btnBox = new VBox();
		Button done = new Button("Done");
		done.setOnAction(e -> {
			try (DataOutputStream output = new DataOutputStream(new FileOutputStream(userData));) {
				output.writeChars(nameField.getCharacters().toString() + "\n");
				output.writeDouble(Double.parseDouble(budgetField.getCharacters().toString()));
				output.close();
				primaryStage.hide();
				start(primaryStage);
			} catch (IOException ex) {
				System.out.println("Error with Data Writing");
			} catch(NumberFormatException ex) {
				throwErrorStage("Budget must be filled with a number");
			}
		});
		btnBox.getChildren().add(done);
		btnBox.setAlignment(Pos.CENTER);
		splashBorder.setBottom(btnBox);
		
		splashBorder.setBackground(new Background(new BackgroundFill(Color.SKYBLUE,null,null)));
		
		primaryStage.setTitle("Splash Screen");
		return splashBorder;
	}
	
	protected BorderPane getDataScreen(Stage primaryStage) {
		BorderPane dataBorder = new BorderPane();
	//	Center: stats and data   NOT DONE AT ALL
	//	
		dataBorder.setTop(getNavbar(primaryStage));
		dataBorder.setCenter(getMainStats(primaryStage));
		dataBorder.setBottom(getValueList(primaryStage));
		
		dataBorder.setBackground(new Background(new BackgroundFill(Color.LIGHTSLATEGREY,null,null)));
		
		primaryStage.setTitle("Main Menu");
		return dataBorder;
	}
	
	protected BorderPane getStatsScreen(Stage primaryStage) {
		BorderPane statsBorder = new BorderPane();
		statsBorder.setTop(getNavbar(primaryStage));
		statsBorder.setCenter(getStatsPage());
		
		primaryStage.setTitle("Stats n Stuff");
		return statsBorder;
	}
	
	protected HBox getNavbar(Stage primaryStage) {
		HBox navbar = new HBox();
		int height = 50;
		navbar.setBackground(new Background(new BackgroundFill(Color.SKYBLUE,null,null)));

		StackPane stack1 = new StackPane();
		StackPane stack2 = new StackPane();
		Label label1 = new Label("$AVINGS");
		Label label2 = new Label("$TATS");
		label1.setFont(fontB);
		label2.setFont(fontB);
		Rectangle rec1 = new Rectangle(width/6,height,Color.SKYBLUE);
		Rectangle rec2 = new Rectangle(width/6,height,Color.SKYBLUE);
		
		stack1.getChildren().addAll(rec1,label1);
		stack2.getChildren().addAll(rec2,label2);
		
		stack1.setOnMouseClicked(e -> {
			primaryStage.setScene(new Scene(getDataScreen(primaryStage),width, this.height));
		});
		stack2.setOnMouseClicked(e -> {
			primaryStage.setScene(new Scene(getStatsScreen(primaryStage),width, this.height));
		});
		
		stack1.setOnMouseEntered(e -> {
			rec1.setFill(Color.SKYBLUE.brighter().brighter());
		});
		stack2.setOnMouseEntered(e -> {
			rec2.setFill(Color.SKYBLUE.brighter().brighter());
		});
		
		stack1.setOnMouseExited(e -> {
			rec1.setFill(Color.SKYBLUE);
		});
		stack2.setOnMouseExited(e -> {
			rec2.setFill(Color.SKYBLUE);
		});
		
		navbar.getChildren().addAll(stack1,stack2);
		return navbar;
	}
	
	protected HBox getMainStats(Stage primaryStage) {
		HBox root = new HBox(0);
		VBox text = new VBox();
		text.setPadding(new Insets(0,0,0,50));
		Label welcome = new Label("Welcome Back " + name);
		Label remainingMoneyText = new Label("Money Remaining");
		Label remainingMoney = new Label(String.format("$%.2f", budget - values.getListSpendings() ) );
		Label spentMoneyText = new Label("Money Spent");
		Label spentMoney = new Label(String.format("$%.2f", values.getListSpendings() ) );
		
		welcome.setFont(new Font("Agency FB", 35));
		welcome.setAlignment(Pos.TOP_CENTER);
		welcome.setUnderline(true);
		
		remainingMoneyText.setFont(font2);
		remainingMoney.setFont(font2b);
		remainingMoney.setTextFill(Color.LIGHTGREEN);
		spentMoneyText.setFont(font2);
		spentMoney.setFont(font2b);
		spentMoney.setTextFill(Color.RED);
		
		text.setAlignment(Pos.CENTER);
		text.getChildren().addAll(welcome,remainingMoneyText, remainingMoney, spentMoneyText, spentMoney);
		
		ObservableList<PieChart.Data> moneyStats = FXCollections.observableArrayList(
				new PieChart.Data("Spent", values.getListSpendings()),
				new PieChart.Data("Available", budget - values.getListSpendings() ));
		
		PieChart money = new PieChart(moneyStats);
		money.getData().get(0).getNode().setStyle("-fx-pie-color: chocolate;");
		money.getData().get(1).getNode().setStyle("-fx-pie-color: limegreen;");
		
		money.setLabelsVisible(false);
		money.setLegendVisible(true);
		
		root.getChildren().addAll(text,money);		
		root.setAlignment(Pos.CENTER);
		
		return root;
	}
	
	protected VBox getValueList(Stage primaryStage) {
		VBox buttonAndList = new VBox();
		
		GridPane dataGrid = new GridPane();
		
		ScrollPane dataScroll = new ScrollPane(dataGrid);
		dataScroll.setPannable(true);
		dataScroll.setFitToWidth(true);
		dataScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		
		Button newEntry = new Button("New Entry");
		newEntry.setOnAction(e ->{
			Stage stage = new Stage();
			stage.setScene(new Scene(getNewEntryScreen(primaryStage,stage),218 ,150));
			stage.show();
		});
		
		StackPane stackHead1 = new StackPane();
		StackPane stackHead2 = new StackPane();
		StackPane stackHead3 = new StackPane();
		StackPane stackHead4 = new StackPane();
		StackPane stackHead5 = new StackPane();
		
		Label head1 = new Label("Description");
		Label head2 = new Label("Date");
		Label head3 = new Label("Category"); 
		Label head4 = new Label("Cost");
		Label head5 = new Label("");
		
		head1.setFont(fontB);
		head2.setFont(fontB);
		head3.setFont(fontB);
		head4.setFont(fontB);
		head5.setFont(fontB);
		
		//for sizing
		Rectangle recHead1 = new Rectangle(6*width/12,.4);
		Rectangle recHead2 = new Rectangle(1.5*width/12,.4);
		Rectangle recHead3 = new Rectangle(2.25*width/12,.4);
		Rectangle recHead4 = new Rectangle(1.5*width/12,.4);
		Rectangle recHead5 = new Rectangle(.5*width/12,.4);
		
		stackHead1.getChildren().addAll(recHead1,head1);
		stackHead2.getChildren().addAll(recHead2,head2);
		stackHead3.getChildren().addAll(recHead3,head3);
		stackHead4.getChildren().addAll(recHead4,head4);
		stackHead5.getChildren().addAll(recHead5,head5);
		
		stackHead1.setAlignment(Pos.BASELINE_CENTER);
		stackHead2.setAlignment(Pos.BASELINE_LEFT);
		stackHead3.setAlignment(Pos.BASELINE_LEFT);
		stackHead4.setAlignment(Pos.BASELINE_LEFT);
		stackHead5.setAlignment(Pos.BASELINE_LEFT);
		
		dataGrid.add(stackHead1, 0, 0);
		dataGrid.add(stackHead2, 1, 0);
		dataGrid.add(stackHead3, 2, 0);
		dataGrid.add(stackHead4, 3, 0);
		dataGrid.add(stackHead5, 4, 0);
		
		for(int i = 0; i < values.size(); i++) {
			StackPane descSP = new StackPane();
			StackPane catSP = new StackPane();
			StackPane amntSP = new StackPane();
			StackPane dateSP = new StackPane();
			
			Label desc = new Label(" " + values.get(i).getDesc());
			Label cat = new Label(values.get(i).getCategory());
			Label amnt = new Label(String.format("$%.2f",values.get(i).getAmnt()));
			Label date = new Label(values.get(i).getCal().getTime().getMonth() + 1 + "/" + values.get(i).getCal().getTime().getDate());
			
			Button delete = new Button("X");
			delete.setPrefSize(50,5);
			delete.setOnAction(e -> {
				int row = GridPane.getRowIndex(delete);
				values.remove(row-2);
				primaryStage.setScene(new Scene(getDataScreen(primaryStage), width, height));
				try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(monthData))) {
					output.writeObject(values);
					output.close();
				}catch (IOException ex) {}
			});
			
			desc.setFont(font);
			cat.setFont(font);
			amnt.setFont(font);
			date.setFont(font);
			
			descSP.getChildren().addAll(new Rectangle(6*width/12,.4), desc);
			catSP.getChildren().addAll(new Rectangle(2.25*width/12,.4), cat);
			amntSP.getChildren().addAll(new Rectangle(1.5*width/12,.4), amnt);
			dateSP.getChildren().addAll(new Rectangle(1.5*width/12,.4),date);
			
			descSP.setAlignment(Pos.BASELINE_LEFT);
			catSP.setAlignment(Pos.BASELINE_LEFT);
			amntSP.setAlignment(Pos.BASELINE_LEFT);
			dateSP.setAlignment(Pos.BASELINE_LEFT);
			delete.setAlignment(Pos.TOP_RIGHT);
			
			dataGrid.add(descSP, 0, i+2);
			dataGrid.add(dateSP, 1, i+2);
			dataGrid.add(catSP, 2, i+2);
			dataGrid.add(amntSP, 3, i+2);
			dataGrid.add(delete, 4, i+2);
		}
		dataGrid.setPrefSize(width, height/2);
		dataGrid.setMaxSize(width, height/2);
		dataGrid.setBackground(new Background(new BackgroundFill(Color.LIGHTSLATEGRAY,null,null)));
		
		buttonAndList.getChildren().addAll(newEntry,dataScroll);
		
		return buttonAndList;
	}
	
	protected HBox getStatsPage() {
		HBox statsBox = new HBox();
		statsBox.setBackground(new Background(new BackgroundFill(Color.LIGHTSLATEGRAY,null,null)));
		double[][] catCount = new double[2][7];
		
		for(int i = 0; i < values.size(); i++) {
			switch (values.get(i).getCategory()) {
			case "Food":	catCount[0][0]++;
							catCount[1][0] += values.get(i).getAmnt();
							break;
			case "Fun":		catCount[0][1]++;
							catCount[1][1] += values.get(i).getAmnt();
							break;
			case "Clothes": catCount[0][2]++;
							catCount[1][2] += values.get(i).getAmnt();
							break;
			case "Subscription": 	catCount[0][3]++;
									catCount[1][3] += values.get(i).getAmnt();
									break;
			case "Transportation": 	catCount[0][4]++;
									catCount[1][4] += values.get(i).getAmnt();
									break;
			case "Other":	catCount[0][5]++;
							catCount[1][5] += values.get(i).getAmnt();
							break;
			case "Health & Beauty":	catCount[0][6]++;
									catCount[1][6] += values.get(i).getAmnt();
									break;
			}
		}
		
		ObservableList<PieChart.Data> catStats = FXCollections.observableArrayList(
				new PieChart.Data("Food", catCount[0][0]),
				new PieChart.Data("Leisure", catCount[0][1]),
				new PieChart.Data("Clothes", catCount[0][2]),
				new PieChart.Data("Subscription", catCount[0][3]),
				new PieChart.Data("Transportation", catCount[0][4]),
				new PieChart.Data("Other", catCount[0][5]),
				new PieChart.Data("Health & Beauty", catCount[0][6]));
		
		PieChart freqStats = new PieChart(catStats);
		freqStats.setStyle("-fx-font-family: Agency FB;");
		freqStats.setTitle("Frequency of Categories");
		statsBox.getChildren().add(freqStats);
		
		ObservableList<PieChart.Data> catSpending = FXCollections.observableArrayList(
				new PieChart.Data("Food", catCount[1][0]),
				new PieChart.Data("Leisure", catCount[1][1]),
				new PieChart.Data("Clothes", catCount[1][2]),
				new PieChart.Data("Subscription", catCount[1][3]),
				new PieChart.Data("Transportation", catCount[1][4]),
				new PieChart.Data("Other", catCount[1][5]),
				new PieChart.Data("Health & Beauty", catCount[1][6]),
				new PieChart.Data("Unspent", budget - values.getListSpendings()));
		
		PieChart spendStats = new PieChart(catSpending);
		spendStats.setTitle("Amount Spent per Category");
		statsBox.getChildren().add(spendStats);
		
		return statsBox;
	}
	
	protected VBox getNewEntryScreen(Stage primaryStage, Stage stage) {
		VBox newEntryVBox = new VBox();
		
		GridPane newEntryGrid = new GridPane();
		newEntryVBox.getChildren().add(newEntryGrid);
		newEntryGrid.setVgap(3);
		
		Label desc = new Label("Description: ");
		TextField descField = new TextField();
		Label cost = new Label("Cost: ");
		TextField costField = new TextField();
		
		newEntryGrid.add(desc, 0, 0);
		newEntryGrid.add(descField, 1, 0);
		newEntryGrid.add(cost, 0, 1);
		newEntryGrid.add(costField, 1, 1);
		
		FlowPane catButtons = new FlowPane(10,10);
		catButtons.setMaxWidth(265);
		newEntryVBox.getChildren().add(catButtons);
		catButtons.setAlignment(Pos.CENTER);
		
		ToggleGroup cat = new ToggleGroup();
		
		RadioButton food = new RadioButton("Food");
		RadioButton fun = new RadioButton("Leisure");
		RadioButton health = new RadioButton("Health & Beauty");
		RadioButton clothes = new RadioButton("Clothes");
		RadioButton subscription = new RadioButton("Subscription");
		RadioButton transportation = new RadioButton("Transportation");
		RadioButton other = new RadioButton("Other");
		
		food.setToggleGroup(cat);
		fun.setToggleGroup(cat);
		clothes.setToggleGroup(cat);
		health.setToggleGroup(cat);
		subscription.setToggleGroup(cat);
		transportation.setToggleGroup(cat);
		other.setToggleGroup(cat);
		
		catButtons.getChildren().addAll(food,fun,clothes,health,subscription,transportation,other);
		
		Button done = new Button("Done");
		done.setOnAction(e -> {
			try {
				values.add(new MoneyData(Double.parseDouble(costField.getText()), descField.getText() , ((RadioButton)cat.getSelectedToggle()).getText()));
				try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(monthData))){
					output.writeObject(values);
					output.close();
					primaryStage.setScene(new Scene(getDataScreen(primaryStage), width, height));
					stage.close();
				}catch (IOException ex) {
					System.out.println("Error while Object Writing");
					System.exit(0);
				}
			}catch(NumberFormatException ex) {
				throwErrorStage("Cost must be filled with a number");
			}catch(NullPointerException ex) {
				throwErrorStage("Select a category");
			}
		});
		newEntryVBox.getChildren().add(done);
		newEntryVBox.setAlignment(Pos.CENTER);
		
		newEntryVBox.setBackground(new Background(new BackgroundFill(Color.ALICEBLUE, null, null)));
		descField.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, null,null)));
		costField.setBackground(new Background(new BackgroundFill(Color.SKYBLUE, null,null)));
		
		return newEntryVBox;
	}
	
	private void throwErrorStage(String s) {
		Stage stage = new Stage();
		VBox error = new VBox(5);
		Label errorMessage = new Label(s);
		Button btn = new Button("Okay");

		btn.setOnAction(e -> {
			stage.close();
		});
		error.setAlignment(Pos.CENTER);
		error.getChildren().addAll(errorMessage,btn);
		stage.setScene(new Scene(error, 400 , 100));
		stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}