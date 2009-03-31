package com.lottodroid.communication;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.lottodroid.model.Bonoloto;
import com.lottodroid.model.Lottery;
import com.lottodroid.model.Quiniela;

/**
 * Implementation for {@link LotteryFetcher} that uses in-memory, hard-coded data.
 */
public class MockLotteryFetcher implements LotteryFetcher {

  @Override
  public List<Lottery> retrieveLastAllLotteries() {
    List<Lottery> listLottery = new LinkedList<Lottery>();
    listLottery.add(new Bonoloto(new Date(), "5 6 7 1 0 9", "4", "9"));

    Quiniela quiniela = new Quiniela(new Date());
    quiniela.setMatch(0, "Barcelona", "Villareal", "X");
    quiniela.setMatch(1, "R. Madrid", "Villareal", "2");

    listLottery.add(quiniela);

    listLottery.add(new Bonoloto(new Date(), "5 55 7 1 0 9", "3", "4"));
    listLottery.add(new Bonoloto(new Date(), "5 6 7 1 66 9", "6", "9"));
    listLottery.add(new Bonoloto(new Date(), "1 2 3 4 5 6", "1", "0"));

    return listLottery;
  }

  @Override
  public List<Bonoloto> retrieveLastBonolotos(int start, int limit) {
    List<Bonoloto> listBonoloto = new LinkedList<Bonoloto>();
    listBonoloto.add(new Bonoloto(new Date(), "5 6 7 1 0 9", "4", "9"));
    listBonoloto.add(new Bonoloto(new Date(), "5 55 7 1 0 9", "3", "4"));
    listBonoloto.add(new Bonoloto(new Date(), "5 6 7 1 66 9", "6", "9"));

    return listBonoloto;
  }

  @Override
  public List<Quiniela> retrieveLastQuinielas(int start, int limit) {
    List<Quiniela> listQuiniela = new LinkedList<Quiniela>();

    Quiniela quiniela = new Quiniela(new Date());
    quiniela.setMatch(0, "Barcelona", "Villareal", "X");
    quiniela.setMatch(1, "Betis", "Villareal", "1");
    quiniela.setMatch(2, "R. Madrid", "Villareal", "2");

    listQuiniela.add(quiniela);

    Quiniela quiniela2 = new Quiniela(new Date());
    quiniela2.setMatch(0, "Barcelona", "Villareal", "X");
    quiniela2.setMatch(1, "Betis", "Villareal", "1");
    quiniela2.setMatch(2, "R. Madrid", "Villareal", "2");

    listQuiniela.add(quiniela2);

    Quiniela quiniela3 = new Quiniela(new Date());
    quiniela3.setMatch(0, "Barcelona", "Villareal", "X");
    quiniela3.setMatch(1, "Betis", "Villareal", "1");
    quiniela3.setMatch(2, "R. Madrid", "Villareal", "2");

    listQuiniela.add(quiniela3);

    return listQuiniela;
  }

}
