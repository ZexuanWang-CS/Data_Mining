package Apriori;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class AprioriPlus {

	/* Plus 1: If the count-so-far passes the minimum support, 
	 * the itemset is added into the frequent itemset collection 
	 * and can be used to generate longer candidates.
	 * 
	 * Plus 2: Transaction reduction.
	 */

	public static ArrayList<ArrayList<String>> AllPeo;
	public static ArrayList<HashSet<String>> AllPeoSet;
	public static HashMap<String, Integer> FinalResMap;
	public static int sup;

	public static void main(String[] args) throws IOException {
		long start = System.nanoTime();
		URL url = new URL("https://archive.ics.uci.edu/ml/machine-learning-databases/adult/adult.data");
		//		URL url = new URL("https://archive.ics.uci.edu/ml/machine-learning-databases/adult/adult.test");
		Scanner SC = new Scanner(url.openStream());
		AllPeo = new ArrayList<ArrayList<String>>();
		AllPeoSet = new ArrayList<HashSet<String>>();
		FinalResMap = new HashMap<String, Integer>();
		int k = 0;
		while(SC.hasNextLine()) {
			ArrayList<String> SinPeo = new ArrayList<String>();
			String SinPeoStr = SC.nextLine();
			if (!SinPeoStr.isEmpty()) {
				SinPeo = SinPeoAttr(SinPeoStr);
				if (!(SinPeo == null)) {
					AllPeo.add(SinPeo);
				}
			}
		}
		System.out.println("The dataset has a size of " + AllPeo.size());
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			AllPeo_ele.set(1, "wrk" + AllPeo_ele.get(1));
			AllPeo_ele.set(3, "edu" + AllPeo_ele.get(3));
			AllPeo_ele.set(5, "mar" + AllPeo_ele.get(5));
			AllPeo_ele.set(6, "ocp" + AllPeo_ele.get(6));
			AllPeo_ele.set(7, "rlt" + AllPeo_ele.get(7));
			AllPeo_ele.set(8, "rac" + AllPeo_ele.get(8));
			AllPeo_ele.set(13, "nat" + AllPeo_ele.get(13));
		}
		int age_min = 100;
		int age_max = 0;
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			if (Integer.valueOf(AllPeo_ele.get(0))<age_min) {
				age_min = Integer.valueOf(AllPeo_ele.get(0));
			}
			if (Integer.valueOf(AllPeo_ele.get(0))>age_max) {
				age_max = Integer.valueOf(AllPeo_ele.get(0));
			}
		}
		System.out.println("The Min Age is " + age_min); //age_min: 17
		System.out.println("The Max Age is " + age_max); //age_max: 90
		//age1: 11-20; age2: 21-30... age8: 81-90.
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			AllPeo_ele.set(0, "age"+String.valueOf((Integer.valueOf(AllPeo_ele.get(0))-1)/10));
		}
		int fwg_min = 10000000;
		int fwg_max = -10000000;
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			if (Integer.valueOf(AllPeo_ele.get(2))<fwg_min) {
				fwg_min = Integer.valueOf(AllPeo_ele.get(2));
			}
			if (Integer.valueOf(AllPeo_ele.get(2))>fwg_max) {
				fwg_max = Integer.valueOf(AllPeo_ele.get(2));
			}
		}
		System.out.println("The Min Weight is " + fwg_min); //fwg_min: 13769
		System.out.println("The Max Weight is " + fwg_max); //fwg_max: 1484705
		//fwg1: <=99000; fwg2: <=198000... fwg4: >297000.
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			if (Integer.valueOf(AllPeo_ele.get(2))<=99000) {
				AllPeo_ele.set(2, "fwg1");
			}
			else if (Integer.valueOf(AllPeo_ele.get(2))<=198000) {
				AllPeo_ele.set(2, "fwg2");
			}
			else if (Integer.valueOf(AllPeo_ele.get(2))<=297000) {
				AllPeo_ele.set(2, "fwg3");
			}
			else {
				AllPeo_ele.set(2, "fwg4");
			}
		}
		int edu_min = 100;
		int edu_max = 0;
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			if (Integer.valueOf(AllPeo_ele.get(4))<edu_min) {
				edu_min = Integer.valueOf(AllPeo_ele.get(4));
			}
			if (Integer.valueOf(AllPeo_ele.get(4))>edu_max) {
				edu_max = Integer.valueOf(AllPeo_ele.get(4));
			}
		}
		System.out.println("The Min education-num is " + edu_min); //edu_min: 1
		System.out.println("The Max education-num is " + edu_max); //edu_max: 16
		//edu1: <=2; edu2: <=4... edu8: <=16.
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			AllPeo_ele.set(4, "edu"+String.valueOf((Integer.valueOf(AllPeo_ele.get(4))-1)/2+1));
		}
		int capgn_min = 100000;
		int capgn_max = 0;
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			if (Integer.valueOf(AllPeo_ele.get(10))<capgn_min) {
				capgn_min = Integer.valueOf(AllPeo_ele.get(10));
			}
			if (Integer.valueOf(AllPeo_ele.get(10))>capgn_max) {
				capgn_max = Integer.valueOf(AllPeo_ele.get(10));
			}
		}
		System.out.println("The Min capital-gain is " + capgn_min); //capgn_min: 0
		System.out.println("The Max capital-gain is " + capgn_max); //capgn_max: 99999
		//capgn1: <1*10^4; capgn2: <2*10^4... capgn10: <10*10^4.
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			AllPeo_ele.set(10, "capgn"+String.valueOf((Integer.valueOf(AllPeo_ele.get(10))/10000+1)));
		}
		int capls_min = 100000;
		int capls_max = 0;
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			if (Integer.valueOf(AllPeo_ele.get(11))<capls_min) {
				capls_min = Integer.valueOf(AllPeo_ele.get(11));
			}
			if (Integer.valueOf(AllPeo_ele.get(11))>capls_max) {
				capls_max = Integer.valueOf(AllPeo_ele.get(11));
			}
		}
		System.out.println("The Min capital-loss is " + capls_min); //capls_min: 0
		System.out.println("The Max capital-loss is " + capls_max); //capls_max: 99999
		//capls1: <500; capls2: <1000... capls9: <4500.
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			AllPeo_ele.set(11, "capls"+String.valueOf((Integer.valueOf(AllPeo_ele.get(11))/500+1)));
		}
		int hou_min = 100;
		int hou_max = 0;
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			if (Integer.valueOf(AllPeo_ele.get(12))<hou_min) {
				hou_min = Integer.valueOf(AllPeo_ele.get(12));
			}
			if (Integer.valueOf(AllPeo_ele.get(12))>hou_max) {
				hou_max = Integer.valueOf(AllPeo_ele.get(12));
			}
		}
		System.out.println("The Min hours-per-week is " + hou_min); //hou_min: 0
		System.out.println("The Max hours-per-week is " + hou_max); //hou_max: 99999
		//hou1: <10; hou2: <20... hou10: <100.
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			AllPeo_ele.set(12, "hou"+String.valueOf((Integer.valueOf(AllPeo_ele.get(12))/10+1)));
		}
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			HashSet<String> AllPeo_eleSet= (HashSet<String>)AllPeo_ele.stream().collect(Collectors.toSet());
			AllPeoSet.add(AllPeo_eleSet);
		}
		long end = System.nanoTime();
		long runtime = (end - start)/1000000;
		System.out.println("\n" + "The running time for preprocessing is " + runtime + "ms");
		//support: 30162*0.2=6033
		start = System.nanoTime();
		sup = 7000;
		HashMap<String, Integer> AllPeoAttrk1 = new HashMap<String, Integer>();
		for (ArrayList<String> AllPeo_ele : AllPeo) {
			for (String AllPeo_ele_ele : AllPeo_ele) {
				if (AllPeoAttrk1.containsKey(AllPeo_ele_ele)) {
					Integer val = AllPeoAttrk1.get(AllPeo_ele_ele)+1;
					AllPeoAttrk1.put(AllPeo_ele_ele, val);
				}
				else {
					AllPeoAttrk1.put(AllPeo_ele_ele, 0);
				}
			}
		}
		HashMap<String, Integer> AllPeoAttrk1CP = DeepCopy(AllPeoAttrk1);
		Iterator ItAttrk1 = AllPeoAttrk1CP.entrySet().iterator();
		while (ItAttrk1.hasNext()) {
			Map.Entry pair = (Map.Entry)ItAttrk1.next();
			ItAttrk1.remove();
			if ((int)pair.getValue() < sup) {
				AllPeoAttrk1.remove(pair.getKey(), pair.getValue());
			}
		}
		FinalResMap.putAll(AllPeoAttrk1);
		AllPeoSet = TransReduce(AllPeoSet, AllPeoAttrk1);
		HashMap<String, Integer> Res = new HashMap<String, Integer>();
		Res = AprioriCandidGen(AllPeoAttrk1);
		Res = CountAndFilter(Res);
		int Kround = 1;
		while (!Res.isEmpty()) {
			Kround++;
			FinalResMap.putAll(Res);
			AllPeoSet = TransReduce(AllPeoSet, Res);
			Res = AprioriCandidGen(Res);
			Res = CountAndFilter(Res);
		}
		System.out.println("\n" + "The final frequent " + "1 to " + Kround + " itemsets are below" + "\n");
		Set<String> FinalResMapKeySet = FinalResMap.keySet();
		for (String FinalResMapKeySet_ele : FinalResMapKeySet) {
			int Num = FinalResMap.get(FinalResMapKeySet_ele);
			System.out.println("[" + FinalResMapKeySet_ele + "]: " + Num);
		}
		System.out.println("\n" + "Size is " + FinalResMapKeySet.size());
		end = System.nanoTime();
		runtime = (end - start)/1000000;
		System.out.println("\n" + "The running time for Apriori is " + runtime + "ms");
	}

	public static ArrayList<String> SinPeoAttr(String sinpeostr) {
		ArrayList<String> sinpeo = new ArrayList<String>();
		if (sinpeostr.contains("?")) {
			return null;
		}
		else {
			String[] sinpeostrSp = sinpeostr.split(",\\s");
			for (int i = 0; i < sinpeostrSp.length; i++) {
				sinpeo.add(sinpeostrSp[i]);
			}
			return sinpeo;
		}
	}

	public static HashMap<String, Integer> DeepCopy(HashMap<String, Integer> hashMapOri) {
		HashMap<String, Integer> hashMapNew = new HashMap<String, Integer>();
		for (Map.Entry<String, Integer> hashMapOri_ele : hashMapOri.entrySet()) {
			hashMapNew.put(hashMapOri_ele.getKey(), hashMapOri_ele.getValue());
		}
		return hashMapNew;
	}

	public static ArrayList<HashSet<String>> DeepCopy2(ArrayList<HashSet<String>> allPeoset) {
		ArrayList<HashSet<String>> allPeosetNew = new ArrayList<HashSet<String>>();
		for (HashSet<String> allPeoset_ele : allPeoset) {
			allPeosetNew.add(allPeoset_ele);
		}
		return allPeosetNew;
	}

	public static HashMap<String, Integer> AprioriCandidGen(HashMap<String, Integer> AllPeoAttrbf) {
		if (AllPeoAttrbf.isEmpty()) {
			return null;
		}
		HashMap<String, Integer> AllPeoAttrbfCP = DeepCopy(AllPeoAttrbf);
		HashMap<String, Integer> AllPeoAttraf = new HashMap<String, Integer>();
		Set<String> AllPeoAttrbfCPkey = new HashSet<String>();
		AllPeoAttrbfCPkey = AllPeoAttrbfCP.keySet();
		ArrayList<String> AllPeoAttrbfCPkeyArr = new ArrayList<String>();
		Object[] AllPeoAttrbfCPkeyarr = AllPeoAttrbfCPkey.toArray();
		for (int i = 0; i < AllPeoAttrbfCPkeyarr.length; i++) {
			AllPeoAttrbfCPkeyArr.add(i, (String) AllPeoAttrbfCPkeyarr[i]);
		}
		for (int i = 0; i < AllPeoAttrbfCPkeyArr.size(); i++) {
			for (int j = i; j < AllPeoAttrbfCPkeyArr.size(); j++) {
				ArrayList<String> itemsetSpt1 = SplitItemset(AllPeoAttrbfCPkeyArr.get(i));
				ArrayList<String> itemsetSpt2 = SplitItemset(AllPeoAttrbfCPkeyArr.get(j));
				int itemNum = itemsetSpt1.size();
				if (itemNum != itemsetSpt2.size()) {
					System.out.println("Check!!!");
				}
				int sam = 0;
				for (int k = 0; k < itemNum; k++) {
					for (int m = 0; m < itemNum; m++) {
						if (itemsetSpt1.get(k).equals(itemsetSpt2.get(m))) {
							sam++;
						}
					}
				}
				if (sam == itemNum-1) {
					int k = 0;
					while (itemsetSpt2.contains(itemsetSpt1.get(k))) {
						k++;
					}
					String item1 = itemsetSpt1.get(k);
					k = 0;
					while (itemsetSpt1.contains(itemsetSpt2.get(k))) {
						k++;
					}
					String item2 = itemsetSpt2.get(k);
					if (!(item1.substring(0, 3).equals(item2.substring(0, 3)))) {
						itemsetSpt1.remove(item1);
						int flag = 0;
						for (int q = 0; q < itemsetSpt1.size(); q++) {
							Set<String> itemsetTMP = itemsetSpt1.stream().collect(Collectors.toSet());
							itemsetTMP.remove(itemsetSpt1.get(q));
							itemsetTMP.add(item1);
							itemsetTMP.add(item2);
							for (int r = 0; r < AllPeoAttrbfCPkeyArr.size(); r++) {
								if (SplitItemset(AllPeoAttrbfCPkeyArr.get(r)).stream().collect(Collectors.toSet()).equals(itemsetTMP)) {
									flag++;
								}
							}

						}
						if (flag == itemNum-1) {
							String PeoAttr = AllPeoAttrbfCPkeyArr.get(j) + " " + item1;
							AllPeoAttraf.put(PeoAttr, 0);
						}
					}
				}
			}
		}
		return AllPeoAttraf;
	}

	public static ArrayList<String> SplitItemset(String Itemset) {
		String[] tmp = Itemset.split("\\s");
		ArrayList<String> tmpArr = new ArrayList<String>();
		for (int i = 0; i < tmp.length; i++) {
			tmpArr.add(i, tmp[i]);
		}
		return tmpArr;
	}

	public static HashMap<String, Integer> CountAndFilter(HashMap<String, Integer> res) {
		if (res.isEmpty()) {
			return res;
		}
		HashMap<String, Integer> resCP = DeepCopy(res);
		Iterator resCPIt = resCP.keySet().iterator();
		while (resCPIt.hasNext()) {
			int keyNum = 0;
			String resCPkey = (String) resCPIt.next();
			resCPIt.remove();
			Set<String> resCPkeySet = SplitItemset(resCPkey).stream().collect(Collectors.toSet());
			for (HashSet<String> AllPeoSet_ele : AllPeoSet) {
				if (AllPeoSet_ele.containsAll(resCPkeySet)) {
					keyNum++;
					if (keyNum >= sup) {
						res.put(resCPkey, keyNum);
						break;
					}
				}
			}
			if (keyNum < sup) {
				res.remove(resCPkey, 0);
			}
		}
		return res;
	}

	public static ArrayList<HashSet<String>> TransReduce(ArrayList<HashSet<String>> allPeoset, HashMap<String, Integer> allPeoattr) {
		ArrayList<HashSet<String>> allPeosetCP = DeepCopy2(allPeoset);
		for (HashSet<String> allPeosetCP_ele : allPeosetCP) {
			boolean flag = false;
			HashMap<String, Integer> allPeoattrCP = DeepCopy(allPeoattr);
			Iterator allPeoattrCPIt = allPeoattrCP.keySet().iterator();
			while (allPeoattrCPIt.hasNext()) {
				String allPeoattrCPkey = (String) allPeoattrCPIt.next();
				allPeoattrCPIt.remove();
				Set<String> allPeoattrCPkeySet = SplitItemset(allPeoattrCPkey).stream().collect(Collectors.toSet());
				if (allPeosetCP_ele.containsAll(allPeoattrCPkeySet)) {
					flag = true;
					break;
				}
			}
			if (flag == false) {
				allPeoset.remove(allPeosetCP_ele);
			}
		}
		System.out.println("Reduced size is " + allPeoset.size());
		return allPeoset;
	}
}