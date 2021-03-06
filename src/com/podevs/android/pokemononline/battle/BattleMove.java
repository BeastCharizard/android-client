package com.podevs.android.pokemononline.battle;

import android.graphics.Color;

import com.podevs.android.pokemononline.DataBaseHelper;
import com.podevs.android.pokemononline.ColorEnums.TypeColor;
import com.podevs.android.pokemononline.pokeinfo.MoveInfo;
import com.podevs.android.utilities.Bais;
import com.podevs.android.utilities.Baos;
import com.podevs.android.utilities.SerializeBytes;

public class BattleMove implements SerializeBytes {
	public byte currentPP = 0;
	public byte totalPP = 0;
	public short num = 0;
	public String name = "No Move";
	public byte type = (byte) Type.Curse.ordinal();
	private String power = "--";
	private String accuracy = "--";
	private String description = "";
	private String effect = "";
	
	public String toString() {
		return name;
	}
	
	public int getColor() {
		String s = TypeColor.values()[getType()].toString();
		s = s.replaceAll(">", "");
		return Color.parseColor(s);
	}
	
	public String getTypeString() {
		return Type.values()[getType()].toString();
	}
	
	public byte getType() {
		return type;
	}
	
	public BattleMove() {}
	
	public BattleMove(int n, DataBaseHelper db) {
		num = (short) n;
		name = MoveInfo.name(db, n);
		try {
			type = new Byte(db.query("SELECT type FROM [Moves] WHERE _id = " + num));
		} catch (NumberFormatException e) {
			type = new Byte((byte)Type.Curse.ordinal());
		}
		try {
			totalPP = (byte)(new Byte(db.query("SELECT pp FROM [Moves] WHERE _id = " + num)) * 1.6);
		} catch (NumberFormatException e) {
			totalPP = 0;
		}
		power = db.query("SELECT power FROM [Moves] WHERE _id = " + num);
		accuracy = db.query("SELECT accuracy FROM [Moves] WHERE _id = " + num);
		//description = db.query("SELECT description FROM [Moves] WHERE _id = " + num);
		effect = db.query("SELECT effect FROM [Moves] WHERE _id = " + num);
	}
	
	public BattleMove(BattleMove bm) {
		currentPP = bm.currentPP;
		totalPP = bm.totalPP;
		num = bm.num;
		name = bm.name;
		type = bm.type;
		power = bm.power;
		accuracy = bm.accuracy;
		description = bm.description;
		effect = bm.effect;
	}
	
	public BattleMove(Bais msg, DataBaseHelper db) {
		this(msg.readShort(), db);
		currentPP = msg.readByte();
		totalPP = msg.readByte();
	}
	
	public void serializeBytes(Baos b) {
		b.putShort(num);
		b.write(currentPP);
		b.write(totalPP);
	}
	
	public String descAndEffects() {
		String s = "";
		s += "Power: " + power;
		s += "\nAccuracy: " + accuracy;
		s += "\n";
		s += "\nDescription: " + description;
		s += "\nEffect: " + effect;
		return s;
	}
}