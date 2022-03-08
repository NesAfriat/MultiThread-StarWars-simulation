package bgu.spl.mics.application;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewok;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.services.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different components of the application, and run the system.
 * In the end, you should output a JSON.
 */

public class Main {
	public static void main(String[] args) {
		Gson gson = new Gson();
		Reader reader = null;
		try {
			reader = new FileReader("./inputPath/input.json");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		GsonInput input =gson.fromJson(reader, GsonInput.class);
		Diary diary = Diary.getDiary();



		for (int i = 1; i <= input.getEwoks(); i++)
			Ewoks.getEwoks().addEwok(new Ewok(i));
		//start
		LeiaMicroservice m = new LeiaMicroservice(input.getAttacks());
		Thread Leia = new Thread(m);
		Thread Han = new Thread(new HanSoloMicroservice());
		Thread C3PO = new Thread(new C3POMicroservice());
		Thread R2D2 = new Thread(new R2D2Microservice(input.getR2D2()));
		Thread lando = new Thread(new LandoMicroservice(input.getLando()));

		lando.start();
		Han.start();
		Leia.start();
		C3PO.start();
		R2D2.start();

		try {
			Leia.join();
			Han.join();
			C3PO.join();
			R2D2.join();
			lando.join();
		} catch (InterruptedException e) {
		}

		gson = new GsonBuilder()
				.excludeFieldsWithModifiers(Modifier.STATIC)
				.create();
		try {
			FileWriter fileWriter = new FileWriter("./outputPath/output.json");
			gson.toJson(diary, fileWriter);
			fileWriter.flush();
			fileWriter.close();
		} catch (Exception fileWriteException) {

		}

	}
}
