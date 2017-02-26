package test.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Data {
	public static List<ITPerson> loadGradationData(){
		List<ITPerson> itPersons = new ArrayList<ITPerson>();
		Goods computer = new Goods();
		computer.setName("电脑");
		computer.setPrice(3000.56f);
		Goods guiter = new Goods();
		guiter.setName("吉它");
		guiter.setPrice(900.15f);
		Goods balls = new Goods();
		balls.setName("篮球");
		balls.setPrice(100.99f);
		
		Language java = new Language();
		java.setCode("java");
		java.setName("java");
		
		Favorite pro = new Favorite();
		pro.setGoods(computer);
		pro.setName("程序");
		
		Favorite lol = new Favorite();
		lol.setGoods(computer);
		lol.setName("英雄联盟");
		
		Favorite music = new Favorite();
		music.setGoods(guiter);
		music.setName("音乐");
		
		Favorite learn = new Favorite();
		learn.setName("学习");
		
		Favorite playBall = new Favorite();
		playBall.setGoods(balls);
		playBall.setName("打球");
		
		List<Favorite> favorites = null;
		
		ITPerson itPerson = new ITPerson();
		itPerson.setAge(1);
		itPerson.setBirthday(new Date());
		itPerson.setLanguage(java);
		itPerson.setName("sanri1993");
		itPerson.setSex(1);
		itPerson.setWork(true);
		favorites = new ArrayList<Favorite>();
		favorites.add(pro);
		favorites.add(playBall);
		favorites.add(music);
		itPerson.setFavorites(favorites);
		itPersons.add(itPerson);
		
		
		itPerson = new ITPerson();
		itPerson.setAge(2);
		itPerson.setBirthday(new Date());
		itPerson.setLanguage(java);
		itPerson.setName("刘治国");
		itPerson.setSex(1);
		itPerson.setWork(false);
		favorites = new ArrayList<Favorite>();
		favorites.add(lol);
		itPerson.setFavorites(favorites);
		itPersons.add(itPerson);
		
		itPerson = new ITPerson();
		itPerson.setAge(3);
		itPerson.setBirthday(new Date());
		itPerson.setLanguage(java);
		itPerson.setName("刘武");
		itPerson.setSex(1);
		itPerson.setWork(true);
		favorites = new ArrayList<Favorite>();
		favorites.add(lol);
		favorites.add(playBall);
		itPerson.setFavorites(favorites);
		itPersons.add(itPerson);
		
		
		itPerson = new ITPerson();
		itPerson.setAge(3);
		itPerson.setBirthday(new Date());
		itPerson.setLanguage(java);
		itPerson.setName("刘颖丽");
		itPerson.setSex(2);
		itPerson.setWork(true);
		favorites = new ArrayList<Favorite>();
		favorites.add(music);
		itPerson.setFavorites(favorites);
		itPersons.add(itPerson);
		
		itPerson = new ITPerson();
		itPerson.setAge(2);
		itPerson.setBirthday(new Date());
		itPerson.setLanguage(java);
		itPerson.setName("李遥");
		itPerson.setSex(1);
		itPerson.setWork(true);
		favorites = new ArrayList<Favorite>();
		favorites.add(lol);
		favorites.add(music);
		favorites.add(playBall);
		itPerson.setFavorites(favorites);
		itPersons.add(itPerson);
		
		return itPersons;
	}
	
	public static List<NoFatherBean> loadNormalData(){
		List<NoFatherBean> beans = new ArrayList<NoFatherBean>();
		NoFatherBean bean = new NoFatherBean();
		bean.setAge(1);
		bean.setBirthday(new Date());
		bean.setFavorite("程序");
		bean.setGoods("电脑");
		bean.setName("sanri1993");
		bean.setPrice(3000.56f);
		bean.setSex(1);
		bean.setWork(true);
		beans.add(bean);
		
		bean = new NoFatherBean();
		bean.setAge(2);
		bean.setBirthday(new Date());
		bean.setFavorite("lol");
		bean.setGoods("电脑");
		bean.setName("刘治国");
		bean.setPrice(3000.56f);
		bean.setSex(1);
		bean.setWork(false);
		beans.add(bean);
		
		bean = new NoFatherBean();
		bean.setAge(3);
		bean.setBirthday(new Date());
		bean.setFavorite("音乐");
		bean.setGoods("吉它");
		bean.setName("李遥");
		bean.setPrice(900.15f);
		bean.setSex(1);
		bean.setWork(true);
		beans.add(bean);
		
		bean = new NoFatherBean();
		bean.setAge(4);
		bean.setBirthday(new Date());
		bean.setFavorite("打球");
		bean.setGoods("篮球");
		bean.setName("刘武");
		bean.setPrice(100.99f);
		bean.setSex(1);
		bean.setWork(true);
		beans.add(bean);
		
		bean = new NoFatherBean();
		bean.setAge(5);
		bean.setBirthday(new Date());
		bean.setFavorite("学习");
		bean.setGoods(null);
		bean.setName("刘颖丽");
		bean.setPrice(0);
		bean.setSex(2);
		bean.setWork(true);
		beans.add(bean);
		
		return beans;
	}
}
