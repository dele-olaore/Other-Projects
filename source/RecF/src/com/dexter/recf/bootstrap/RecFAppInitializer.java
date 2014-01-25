package com.dexter.recf.bootstrap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.Status;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolationException;

import org.jboss.solder.logging.Logger;
import org.jboss.solder.servlet.WebApplication;
import org.jboss.solder.servlet.event.Initialized;

import com.dexter.recf.model.Address;
import com.dexter.recf.model.LocalGovtArea;
import com.dexter.recf.model.Role;
import com.dexter.recf.model.State;
import com.dexter.recf.model.User;
import com.dexter.recf.model.UserProfile;

import jxl.*;

/**
 * An alternative bean used to import seed data into the database when the application is being initialized.
 *
 * @author Dele Olaore
 * */
@Alternative
public class RecFAppInitializer
{
	@PersistenceContext
    private EntityManager entityManager;

    @Inject
    private UserTransaction utx;

    @Inject
    private Logger log;
    
    private String[] letters = new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","1","2","3","4","5","6","7","8","9","0","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};

    private final List<Role> roles = new ArrayList<Role>();
    private final List<State> states = new ArrayList<State>();
    private final List<LocalGovtArea> lgas = new ArrayList<LocalGovtArea>();
    private final List<User> users = new ArrayList<User>();
    private final List<UserProfile> profiles = new ArrayList<UserProfile>();

    public RecFAppInitializer()
    {
    	roles.addAll(Arrays.asList(
                new Role("Administrator"), 
                new Role("Report User"),
                new Role("Candidate")));
    	
    	states.addAll(Arrays.asList(
    			new State("N/A"),
                new State("Abia"), 
                new State("Abuja"),
                new State("Adamawa"),
                new State("Akwa Ibom"),
                new State("Anambra"),
                new State("Bauchi"),
                new State("Bayelsa"),
                new State("Benue"),
                new State("Bornu"),
                new State("Cross River"),
                new State("Delta"),
                new State("Ebonyi"),
                new State("Edo"),
                new State("Ekiti"),
                new State("Enugu"),
                new State("Gombe"),
                new State("Imo"),
                new State("Jigawa"),
                new State("Kaduna"),
                new State("Kano"),
                new State("Katsina"),
                new State("Kebbi"),
                new State("Kogi"),
                new State("Kwara"),
                new State("Lagos"),
                new State("Nasarawa"),
                new State("Niger"),
                new State("Ogun"),
                new State("Ondo"),
                new State("Osun"),
                new State("Oyo"),
                new State("Plateau"),
                new State("Rivers"),
                new State("Sokoto"),
                new State("Taraba"),
                new State("Yobe"),
                new State("Zamfara")));
    }
    
    /**
     * Import seed data when Seam Servlet fires an event notifying observers that the web application is being initialized.
     */
    public void importData(@Observes @Initialized WebApplication webapp)
    {
        log.info("Importing seed data for application " + webapp.getName());
        // use manual transaction control since this is a managed bean
        try
        {
        	//utx.begin();
            // AS7-2045
        	/*entityManager.createQuery("delete from User").executeUpdate();
            entityManager.createQuery("delete from Role").executeUpdate();
            entityManager.createQuery("delete from LocalGovtArea").executeUpdate();
            entityManager.createQuery("delete from State").executeUpdate();
            
            persist(roles);
            persist(states);
            
            addToLGs(new String[]{"N/A"}, "N/A");
            
            addToLGs(new String[]{"Aba North", "Aba South", "Arochukwu", "Bende", "Ikwuano", "Isiala-Ngwa North", "Isiala-Ngwa South", "Isuikwato",
            		"Obi Nwa", "Ohafia", "Osisioma", "Ngwa", "Ugwunagbo", "Ukwa East", "Ukwa West", "Umuahia North", "Umuahia South", "Umu-Neochi"}, 
            		"Abia");
            addToLGs(new String[]{"Gwagwalada", "Kuje", "Abaji", "Abuja Municipal", "Bwari", "Kwali"}, 
            		"Abuja");
            addToLGs(new String[]{"Demsa", "Fufore", "Ganaye", "Gireri", "Gombi", "Guyuk", "Hong", "Jada", "Lamurde", "Madagali", "Maiha", "Mayo-Belwa",
            		"Michika", "Mubi North", "Mubi South", "Numan", "Shelleng", "Song", "Toungo", "Yola North", "Yola South"}, 
            		"Adamawa");
            
            addToLGs(new String[]{"Abak","Eastern Obolo","Eket","Esit Eket","Essien Udim","Etim Ekpo","Etinan","Ibeno","Ibesikpo Asutan","Ibiono Ibom",
            		"Ika","Ikono","Ikot Abasi","Ikot Ekpene","Ini","Itu","Mbo","Mkpat Enin","Nsit Atai","Nsit Ibom","Nsit Ubium","Obot Akara","Okobo",
            		"Onna","Oron","Oruk Anam","Udung Uko","Ukanafun","Uruan","Urue-Offong/Oruko","Uyo"}, "Akwa Ibom");
            
            addToLGs(new String[]{"Aguata","Anambra East","Anambra West","Anaocha","Awka North","Awka South","Ayamelum","Dunukofia","Ekwusigo","Idemili North",
            		"Idemili south","Ihiala","Njikoka","Nnewi North","Nnewi South","Ogbaru","Onitsha North","Onitsha South","Orumba North","Orumba South",
            		"Oyi"},"Anambra");
            
            addToLGs(new String[]{"Alkaleri","Bauchi","Bogoro","Damban","Darazo","Dass","Ganjuwa","Giade","Itas/Gadau","Jama'are","Katagum","Kirfi","Misau",
            		"Ningi","Shira","Tafawa-Balewa","Toro","Warji","Zaki"}, "Bauchi");
            
            addToLGs(new String[]{"Alkaleri","Bauchi","Bogoro","Damban","Darazo","Dass","Ganjuwa","Giade","Itas/Gadau","Jama'are","Katagum","Kirfi","Misau",
            		"Ningi","Shira","Tafawa-Balewa","Toro","Warji","Zaki"}, "Bauchi");
            
            addToLGs(new String[]{"Brass","Ekeremor","Kolokuma/Opokuma","Nembe","Ogbia","Sagbama","Southern Jaw","Yenegoa"}, "Bayelsa");
            
            addToLGs(new String[]{"Ado","Agatu","Apa","Buruku","Gboko","Guma","Gwer East","Gwer West","Katsina-Ala","Konshisha","Kwande","Logo",
            		"Makurdi","Obi","Ogbadibo","Oju","Okpokwu","Ohimini","Oturkpo","Tarka","Ukum","Ushongo","Vandeikya"}, "Benue");
            
            addToLGs(new String[]{"Abadam","Askira/Uba","Bama","Bayo","Biu","Chibok","Damboa","Dikwa","Gubio","Guzamala","Gwoza","Hawul","Jere","Kaga",
            		"Kala/Balge","Konduga","Kukawa","Kwaya Kusar","Mafa","Magumeri","Maiduguri","Marte","Mobbar","Monguno","Ngala","Nganzai","Shani"}, "Bornu");
            
            addToLGs(new String[]{"Akpabuyo","Odukpani","Akamkpa","Biase","Abi","Ikom","Yarkur","Odubra","Boki","Ogoja","Yala","Obanliku","Obudu","Calabar South",
            		"Etung","Bekwara","Bakassi","Calabar Municipality"}, "Cross River");
            
            addToLGs(new String[]{"Oshimili","Aniocha","Aniocha South","Ika South","Ika North-East","Ndokwa West","Ndokwa East","Isoko south","Isoko North","Bomadi",
            		"Burutu","Ughelli South","Ughelli North","Ethiope West","Ethiope East","Sapele","Okpe","Warri North","Warri South","Uvwie",
            		"Udu","Warri Central","Ukwani","Oshimili North","Patani"}, "Delta");
            
            addToLGs(new String[]{"Afikpo South","Afikpo North","Onicha","Ohaozara","Abakaliki","Ishielu","lkwo","Ezza","Ezza South","Ohaukwu","Ebonyi",
            		"Ivo"}, "Ebonyi");
            
            addToLGs(new String[]{"Esan North-East","Esan Central","Esan West","Egor","Ukpoba","Central","Etsako Central","Igueben","Oredo","Ovia SouthWest",
            		"Ovia South-East","Orhionwon","Uhunmwonde","Etsako East","Esan South-East"}, "Edo");
            
            addToLGs(new String[]{"Ado","Ekiti-East","Ekiti-West","Emure/Ise/Orun","Ekiti South-West","Ikare","Irepodun","Ijero","Ido/Osi","Oye","Ikole",
            		"Moba","Gbonyin","Efon","Ise/Orun","Ilejemeje"}, "Ekiti");
            
            addToLGs(new String[]{"Enugu South","Igbo-Eze South","Enugu North","Nkanu","Udi Agwu","Oji-River","Ezeagu","IgboEze North","Isi-Uzo",
            		"Nsukka","Igbo-Ekiti","Uzo-Uwani","Enugu Eas","Aninri","Nkanu East","Udenu"}, "Enugu");
            
            addToLGs(new String[]{"Akko","Balanga","Billiri","Dukku","Kaltungo","Kwami","Shomgom","Funakaye","Gombe","Nafada/Bajoga","Yamaltu/Delta"}, "Gombe");
            
            addToLGs(new String[]{"Aboh-Mbaise","Ahiazu-Mbaise","Ehime-Mbano","Ezinihitte","Ideato North","Ideato South","Ihitte/Uboma","Ikeduru",
            		"Isiala Mbano","Isu","Mbaitoli","Mbaitoli","Ngor-Okpala","Njaba","Nwangele","Nkwerre","Obowo","Oguta","Ohaji/Egbema","Okigwe",
            		"Orlu","Orsu","Oru East","Oru West","Owerri-Municipal","Owerri North","Owerri West"}, "Imo");
            
            addToLGs(new String[]{"Auyo","Babura","Birni Kudu","Biriniwa","Buji","Dutse","Gagarawa","Garki","Gumel","Guri","Gwaram","Gwiwa","Hadejia","Jahun",
            		"Kafin Hausa","Kaugama Kazaure","Kiri Kasamma","Kiyawa","Maigatari","Malam Madori","Miga","Ringim","Roni","Sule-Tankarkar","Taura ","Yankwashi"}, "Jigawa");
            
            addToLGs(new String[]{"Birni-Gwari","Chikun","Giwa","Igabi","Ikara","jaba","Jema'a","Kachia","Kaduna North","Kaduna South","Kagarko","Kajuru",
            		"Kaura","Kauru","Kubau","Kudan","Lere","Makarfi","Sabon-Gari","Sanga","Soba","Zango-Kataf","Zaria"}, "Kaduna");
            
            addToLGs(new String[]{"Ajingi","Albasu","Bagwai","Bebeji","Bichi","Bunkure","Dala","Dambatta","Dawakin Kudu","Dawakin Tofa","Doguwa",
            		"Fagge","Gabasawa","Garko","Garum","Mallam","Gaya","Gezawa","Gwale","Gwarzo","Kabo","Kano Municipal","Karaye","Kibiya",
            		"Kiru","kumbotso","Kunchi","Kura","Madobi","Makoda","Minjibir","Nasarawa","Rano","Rimin Gado","Rogo","Shanono",
            		"Sumaila","Takali","Tarauni","Tofa","Tsanyawa","Tudun Wada","Ungogo","Warawa","Wudil"}, "Kano");
            
            addToLGs(new String[]{"Bakori","Batagarawa","Batsari","Baure","Bindawa","Charanchi","Dandume","Danja","Dan Musa","Daura","Dutsi","Dutsin-Ma",
            		"Faskari","Funtua","Ingawa","Jibia","Kafur","Kaita","Kankara","Kankia","Katsina","Kurfi","Kusada","Mai'Adua","Malumfashi","Mani",
            		"Mashi","Matazuu","Musawa","Rimi","Sabuwa","Safana","Sandamu","Zango"}, "Katsina");
            
            addToLGs(new String[]{"Aleiro","Arewa-Dandi","Argungu","Augie","Bagudo","Birnin Kebbi","Bunza","Dandi","Fakai","Gwandu","Jega","Kalgo","Koko/Besse",
            		"Maiyama","Ngaski","Sakaba","Shanga","Suru","Wasagu/Danko","Yauri","Zuru"}, "Kebbi");
            
            addToLGs(new String[]{"Adavi","Ajaokuta","Ankpa","Bassa","Dekina","Ibaji","Idah","Igalamela-Odolu","Ijumu","Kabba/Bunu","Kogi","Lokoja",
            		"Mopa-Muro","Ofu","Ogori/Mangongo","Okehi","Okene","Olamabolo","Omala","Yagba East","Yagba West"}, "Kogi");
            
            addToLGs(new String[]{"Asa","Baruten","Edu","Ekiti","Ifelodun","Ilorin East","Ilorin West","Irepodun","Isin","Kaiama","Moro","Offa",
            		"Oke-Ero","Oyun","Pategi"}, "Kwara");
            
            addToLGs(new String[]{"Agege","Ajeromi-Ifelodun","Alimosho","Amuwo-Odofin","Apapa","Badagry","Epe","Eti-Osa","Ibeju/Lekki","Ifako-Ijaye",
            		"Ikeja","Ikorodu","Kosofe","Lagos Island","Lagos Mainland","Mushin","Ojo","Oshodi-Isolo","Shomolu","Surulere"}, "Lagos");
            
            addToLGs(new String[]{"Akwanga","Awe","Doma","Karu","Keana","Keffi","Kokona","Lafia","Nasarawa","Nasarawa-Eggon","Obi","Toto","Wamba"}, "Nasarawa");
            
            addToLGs(new String[]{"Agaie","Agwara","Bida","Borgu","Bosso","Chanchaga","Edati","Gbako","Gurara","Katcha","Kontagora","Lapai",
            		"Lavun","Magama","Mariga","Mashegu","Mokwa","Muya","Pailoro","Rafi","Rijau","Shiroro","Suleja","Tafa","Wushishi"}, "Niger");
            
            addToLGs(new String[]{"Abeokuta North","Abeokuta South","Ado-Odo/Ota","Egbado North","Egbado South","Ewekoro","Ifo","Ijebu East","Ijebu North","Ijebu North East",
            		"Ijebu Ode","Ikenne","Imeko-Afon","Ipokia","Obafemi-Owode","Ogun Waterside","Odeda","Odogbolu","Remo North","Shagamu"}, "Ogun");
            
            addToLGs(new String[]{"Akoko North East","Akoko North West","Akoko South Akure East","Akoko South West","Akure North","Akure South","Ese-Odo",
            		"Idanre","Ifedore","Ilaje","Ile-Oluji","Okeigbo","Irele","Odigbo","Okitipupa","Ondo East","Ondo West","Ose","Owo"}, "Ondo");
            
            addToLGs(new String[]{"Aiyedade","Aiyedire","Atakumosa East","Atakumosa West","Boluwaduro","Boripe","Ede North","Ede South","Egbedore",
            		"Ejigbo","Ife Central","Ife East","Ife North","Ife South","Ifedayo","Ifelodun","Ila","Ilesha East","Ilesha West","Irepodun",
            		"Irewole","Isokan","Iwo","Obokun","Odo-Otin","Ola-Oluwa","Olorunda","Oriade","Orolu","Osogbo"}, "Osun");
            
            addToLGs(new String[]{"Afijio","Akinyele","Atiba","Atigbo","Egbeda","IbadanCentral","Ibadan North","Ibadan North West","Ibadan South East",
            		"Ibadan South West","Ibarapa Central","Ibarapa East","Ibarapa North","Ido","Irepo","Iseyin","Itesiwaju","Iwajowa","Kajola",
            		"Lagelu Ogbomosho North","Ogbmosho South","Ogo Oluwa","Olorunsogo","Oluyole","Ona-Ara","Orelope","Ori Ire","Oyo East",
            		"Oyo West","Saki East","Saki West","Surulere"}, "Oyo");
            
            addToLGs(new String[]{"Barikin Ladi","Bassa","Bokkos","Jos East","Jos North","Jos South","Kanam","Kanke","Langtang North","Langtang South",
            		"Mangu","Mikang","Pankshin","Qua'an Pan","Riyom","Shendam","Wase"}, "Plateau");
            
            addToLGs(new String[]{"Abua/Odual","Ahoada East","Ahoada West","Akuku Toru","Andoni","Asari-Toru","Bonny","Degema","Emohua","Eleme","Etche",
            		"Gokana","Ikwerre","Khana","Obia/Akpor","Ogba/Egbema/Ndoni","Ogu/Bolo","Okrika","Omumma","Opobo/Nkoro","Oyigbo","Port-Harcourt","Tai"}, "Rivers");
            
            addToLGs(new String[]{"Binji","Bodinga","Dange-shnsi","Gada","Goronyo","Gudu","Gawabawa","Illela","Isa","Kware","kebbe","Rabah","Sabon birni",
            		"Shagari","Silame","Sokoto North","Sokoto South","Tambuwal","Tqngaza","Tureta","Wamako","Wurno","Yabo"}, "Sokoto");
            
            addToLGs(new String[]{"Ardo-kola","Bali","Donga","Gashaka","Cassol","Ibi","Jalingo","Karin-Lamido","Kurmi","Lau","Sardauna","Takum","Ussa","Wukari","Yorro",
            		"Zing"}, "Taraba");
            
            addToLGs(new String[]{"Bade","Bursari","Damaturu","Fika","Fune","Geidam","Gujba","Gulani","Jakusko","Karasuwa","Karawa","Machina","Nangere",
            		"Nguru Potiskum","Tarmua","Yunusari","Yusufari"}, "Yobe");
            
            addToLGs(new String[]{"Anka","Bakura","Birnin Magaji","Bukkuyum","Bungudu","Gummi","Gusau","Kaura","Namoda","Maradun","Maru",
            		"Shinkafi","Talata Mafara","Tsafe","Zurmi"}, "Zamfara");
            
            persist(lgas);
            */
            // Load excel here
            /*String data_upload = "c:/recf/data_upload_.xls"; // webapp.getContextPath() + 
            try
            {
            	System.out.println("Path: " + data_upload);
            	File f = new File(data_upload);
            	System.out.println(f.getAbsolutePath());
            	Workbook wk = Workbook.getWorkbook(f);
            	Sheet sht = wk.getSheet(0);
            	if(sht != null)
            	{
            		Query q = entityManager.createQuery("SELECT e FROM Role e WHERE name = :nm");
        	    	q.setParameter("nm", "Candidate");
        	    	Role cadRole = (Role) q.getSingleResult();
            		
            		int rows = sht.getRows();
            		for(int i=1; i<rows; i++)
            		{
            			String test_id = sht.getCell(1, i).getContents();
            			String lastn = sht.getCell(2, i).getContents();
            			String firstn = sht.getCell(3, i).getContents();
            			String middlen = sht.getCell(4, i).getContents();
            			String email = sht.getCell(5, i).getContents();
            			String tell = sht.getCell(6, i).getContents();
            			String state = sht.getCell(7, i).getContents();
            			String gender = sht.getCell(8, i).getContents();
            			
            			q = entityManager.createQuery("SELECT e FROM State e WHERE name = :nm");
            	    	q.setParameter("nm", "N/A");
            	    	State na_st = null;
            	    	try
            	    	{
            	    		na_st = (State) q.getSingleResult();
            	    	}
            	    	catch(Exception ex)
            	    	{}
            	    	
            	    	q = entityManager.createQuery("SELECT e FROM LocalGovtArea e WHERE name = :nm");
            	    	q.setParameter("nm", "N/A");
            	    	LocalGovtArea na_lg = null;
            	    	try
            	    	{
            	    		na_lg = (LocalGovtArea) q.getSingleResult();
            	    	}
            	    	catch(Exception ex)
            	    	{}
            			
            			UserProfile up = new UserProfile();
            			up.setTestid(test_id);
            			up.setLastname(lastn);
            			up.setFirstname(firstn);
            			up.setMiddlename(middlen);
            			up.setEmail(email);
            			up.setMobileno1(tell);
            			up.setGender(gender);
            			up.setLocalgovtarea(na_lg);
            			up.setState(na_st);
            			up.setLgarea(na_lg);
            			up.setStateoforigin(na_st);
            			
            			q = entityManager.createQuery("SELECT e FROM State e WHERE name = :nm");
            	    	q.setParameter("nm", state);
            	    	try
            	    	{
            	    		State st = (State) q.getSingleResult();
            	    		if(st != null)
            	    			up.setStateoforigin(st);
            	    	}
            	    	catch(Exception ex)
            	    	{}
            	    	
            	    	User u = new User();
            	    	u.setUsername(test_id);
            	    	u.setRole(cadRole);
            	    	u.setPhone("");
            	    	String password = "";
            	    	for(int a=0; a<8; a++)
						{
							Random ran = new Random();
							String l = letters[ran.nextInt(letters.length)];
							password = password + l;
						}
            	    	u.setPassword("password"); //password
            	    	up.setUser(u);
            	    	
            	    	profiles.add(up);*/
            	    	
            	    	/*if(i == (rows-1)) // add extra demo user profiles
            	    	{
            	    		UserProfile up2 = new UserProfile();
                			up2.setTestid("TESTID001");
                			up2.setLastname("TEST001");
                			up2.setFirstname("TEST001");
                			up2.setMiddlename("");
                			up2.setEmail("");
                			up2.setMobileno1("080");
                			up2.setGender("Male");
                			up2.setLocalgovtarea(na_lg);
                			up2.setState(na_st);
                			up2.setLgarea(na_lg);
                			up2.setStateoforigin(na_st);
                			
                			User u2 = new User();
                	    	u2.setUsername("TESTID001");
                	    	u2.setRole(cadRole);
                	    	u2.setPhone("");
                	    	u2.setPassword("PASSWORD");
                	    	up2.setUser(u2);
                	    	
                	    	profiles.add(up2);
                	    	
                	    	up2 = new UserProfile();
                			up2.setTestid("TESTID002");
                			up2.setLastname("TEST002");
                			up2.setFirstname("TEST002");
                			up2.setMiddlename("");
                			up2.setEmail("");
                			up2.setMobileno1("070");
                			up2.setGender("Female");
                			up2.setLocalgovtarea(na_lg);
                			up2.setState(na_st);
                			up2.setLgarea(na_lg);
                			up2.setStateoforigin(na_st);
                			
                			u2 = new User();
                	    	u2.setUsername("TESTID002");
                	    	u2.setRole(cadRole);
                	    	u2.setPhone("");
                	    	u2.setPassword("PASSWORD");
                	    	up2.setUser(u2);
                	    	
                	    	profiles.add(up2);
                	    	
                	    	up2 = new UserProfile();
                			up2.setTestid("TESTID003");
                			up2.setLastname("TEST003");
                			up2.setFirstname("TEST003");
                			up2.setMiddlename("");
                			up2.setEmail("");
                			up2.setMobileno1("081");
                			up2.setGender("Male");
                			up2.setLocalgovtarea(na_lg);
                			up2.setState(na_st);
                			up2.setLgarea(na_lg);
                			up2.setStateoforigin(na_st);
                			
                			u2 = new User();
                	    	u2.setUsername("TESTID003");
                	    	u2.setRole(cadRole);
                	    	u2.setPhone("");
                	    	u2.setPassword("PASSWORD");
                	    	up2.setUser(u2);
                	    	
                	    	profiles.add(up2);
            	    	}*/
            		/*}
            	}
            }
            catch(Exception ex)
            {
            	ex.printStackTrace();
            }*/
            
            /*try
        	{
    	    	Query q = entityManager.createQuery("SELECT e FROM Role e WHERE name = :nm");
    	    	q.setParameter("nm", "Administrator");
    	    	Role admRole = (Role) q.getSingleResult();
    	    	
    	    	q = entityManager.createQuery("SELECT e FROM Role e WHERE name = :nm");
    	    	q.setParameter("nm", "Report User");
    	    	Role ruRole = (Role) q.getSingleResult();
    	    	
    	    	q = entityManager.createQuery("SELECT e FROM Role e WHERE name = :nm");
    	    	q.setParameter("nm", "Candidate");
    	    	Role cadRole = (Role) q.getSingleResult();
    	    	
    	    	User admUser = new User("dele", "dexter", "08026966835", admRole);
    	    	User ruUser = new User("deleru", "dexter", null, ruRole);
    	    	//User cadUser = new User("delecan", "dexter", null, cadRole);
    	    	//User cbnUser = new User("cbn", "password", null, cadRole);
    	    	
    	        users.addAll(Arrays.asList(
    	                admUser, ruUser));
        	}
        	catch(Exception ex)
        	{
        		ex.printStackTrace();
        	}*/
            
            /*persist(users);
            persist(profiles);
            
            utx.commit();
            log.info("Seed data successfully imported");*/
        }
        catch (Exception e)
        {
            /*log.error("Import failed. Seed data will not be available.", e);
            try
            {
                if (utx.getStatus() == Status.STATUS_ACTIVE)
                {
                    try
                    {
                        utx.rollback();
                    }
                    catch (Exception rbe)
                    {
                        log.error("Error rolling back transaction", rbe);
                    }
                }
            }
            catch (Exception se)
            {}*/
        }
    }
    /*
    private void addToLGs(String[] lgs, String state_nm)
    {
    	State state = null;
    	try
    	{
	    	Query q = entityManager.createQuery("SELECT e FROM State e WHERE name = :nm");
	    	q.setParameter("nm", state_nm);
	    	state = (State) q.getSingleResult();
	    	
	    	if(state != null)
	    	{
	    		for(String e : lgs)
	    		{
	    			LocalGovtArea lg = new LocalGovtArea(e);
	    			lg.setState(state);
	    			lgas.add(lg);
	    		}
	    	}
    	}
    	catch(Exception ex)
    	{}
    }*/

    private void persist(List<?> entities)
    {
        for (Object e : entities)
        {
        	if(e instanceof UserProfile)
        	{
        		UserProfile up = (UserProfile)e;
        		System.out.println("test id: " + up.getTestid());
        		User u = up.getUser();
        		persist(u);
        		up.setUser(u);
        		persist(up);
        	}
        	else
        	{
        		persist(e);
        	}
        }
    }

    private void persist(Object entity)
    {
        // use a try-catch block here so we can capture identity
        // of entity that fails to persist
        try
        {
            entityManager.persist(entity);
            entityManager.flush();
        }
        catch(ConstraintViolationException e)
        {
            throw new PersistenceException("Cannot persist invalid entity: " + entity);
        }
        catch(PersistenceException e)
        {
            throw new PersistenceException("Error persisting entity: " + entity, e);
        }
    }
}
