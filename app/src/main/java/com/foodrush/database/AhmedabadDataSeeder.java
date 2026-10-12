package com.foodrush.database;

import com.foodrush.models.FoodItem;
import com.foodrush.models.Restaurant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Authentic Ahmedabad Restaurant Database containing 50+ unique, real-world Ahmedabad food spots,
 * authentic local food areas (Manek Chowk, Law Garden, C.G. Road, SG Highway, Bodakdev, Sindhu Bhavan, etc.),
 * and typical real dishes with realistic prices.
 */
public class AhmedabadDataSeeder {

    public static class AhmedabadRestaurantEntry {
        public Restaurant restaurant;
        public List<FoodItem> foods;

        public AhmedabadRestaurantEntry(Restaurant restaurant, List<FoodItem> foods) {
            this.restaurant = restaurant;
            this.foods = foods;
        }
    }

    public static List<AhmedabadRestaurantEntry> getAhmedabadRestaurants() {
        List<AhmedabadRestaurantEntry> list = new ArrayList<>();

        // Helper dish builders
        list.add(createEntry(
                "ahmd_1", "Agashiye - The House of MG", "Gujarati Thali", "Lal Darwaja",
                "Heritage rooftop fine dining serving authentic Gujarati royal thali & seasonal delicacies.",
                4.8, "35-40 mins", "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=600",
                new FoodItem("ag_1", "Grand Gujarati Royal Thali", "Khaman, Undhiyu, Rotli, Dal, Kadhi, Shrikhand, Farsan", 499, "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=500", "Gujarati"),
                new FoodItem("ag_2", "Kesar Pista Shrikhand", "Velvety hung curd dessert flavoured with Kashmiri saffron", 149, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Dessert"),
                new FoodItem("ag_3", "Surti Undhiyu Bowl", "Traditional slow-cooked mixed winter vegetables with muthias", 229, "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500", "Gujarati")
        ));

        list.add(createEntry(
                "ahmd_2", "Manek Chowk Sandwich Centre", "Street Food", "Manek Chowk",
                "Legendary night street food famous for loaded Gwalior Dosa & Chocolate Pineapple sandwich.",
                4.6, "20-25 mins", "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=600",
                new FoodItem("mc_1", "Cheese Jam Pineapple Sandwich", "Iconic grilled multi-layer sandwich loaded with cheese", 160, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=500", "Sandwich"),
                new FoodItem("mc_2", "Gwalior Butter Dosa", "Ultra-crispy dosa roasted in rich Amul butter with spicy masala", 180, "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=500", "South Indian"),
                new FoodItem("mc_3", "Cadbury Chocolate Sandwich", "Decadent melted Cadbury chocolate inside butter toasted bread", 140, "https://images.unsplash.com/photo-1509722747041-616f39b57569?w=500", "Dessert")
        ));

        list.add(createEntry(
                "ahmd_3", "Das Khaman", "Farsan & Snacks", "Navrangpura",
                "Ahmedabad's legendary snack house serving steam-hot Nylon & Vati Dal Khaman since 1922.",
                4.7, "15-20 mins", "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=600",
                new FoodItem("dk_1", "Nylon Khaman (250g)", "Melt-in-mouth spongy yellow khaman with green chillies & mustard tadka", 80, "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500", "Farsan"),
                new FoodItem("dk_2", "Vati Dal Khaman (250g)", "Coarse chana dal khaman tossed with garlic chutney", 90, "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500", "Farsan"),
                new FoodItem("dk_3", "Tam Tam Sev Khamani", "Spiced crushed khaman garnished with generous sev and pomegranate", 95, "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500", "Farsan")
        ));

        list.add(createEntry(
                "ahmd_4", "Gordhan Thal", "Gujarati Thali", "SG Highway",
                "Grand family dining destination known for authentic Kathiyawadi & Gujarati unlimited thali.",
                4.6, "30-35 mins", "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600",
                new FoodItem("gt_1", "Unlimited Executive Thali", "4 Sabzis, Farsan, Puran Poli, Gujarati Kadhi, Basundi, Chaas", 440, "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=500", "Gujarati"),
                new FoodItem("gt_2", "Puran Poli with Desi Ghee", "Sweet lentil stuffed wholewheat flatbread served with ghee", 120, "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500", "Dessert")
        ));

        list.add(createEntry(
                "ahmd_5", "Laxmi Ganthiya Rath", "Farsan & Snacks", "Nehrunagar",
                "The heart of Ahmedabad breakfast culture: hot Vanela Ganthiya & Sambharo round the clock.",
                4.7, "15-20 mins", "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=600",
                new FoodItem("lg_1", "Vanela Ganthiya (250g)", "Fresh hand-rolled hot ganthiya served with fried chillies and papaya sambharo", 90, "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=500", "Farsan"),
                new FoodItem("lg_2", "Fafda & Jalebi Combo", "Crispy besan fafda with golden hot crispy jalebi & kadhi", 160, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Farsan"),
                new FoodItem("lg_3", "Methi Gota with Kadhi", "Fluffy fenugreek fritters served with tangy hot yellow kadhi", 85, "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500", "Farsan")
        ));

        list.add(createEntry(
                "ahmd_6", "Moti Mahal Deluxe", "North Indian & Mughlai", "Kalupur",
                "Old City pioneer of rich Tandoori delicacies, creamy butter gravies & slow dum biryani.",
                4.4, "30-35 mins", "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=600",
                new FoodItem("mm_1", "Murg Makhani Butter Chicken", "Iconic Delhi-style tender chicken simmered in rich makhani sauce", 340, "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=500", "North Indian"),
                new FoodItem("mm_2", "Tandoori Chicken Platter", "Whole chicken marinated in curd and roasted spices over charcoal", 360, "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=500", "Starters"),
                new FoodItem("mm_3", "Butter Garlic Naan", "Clay oven roasted leavened bread brushed with garlic butter", 65, "https://images.unsplash.com/photo-1621996346565-e3d5d6281057?w=500", "Breads")
        ));

        list.add(createEntry(
                "ahmd_7", "Tomato's", "Continental & American", "C.G. Road",
                "Retro Hollywood themed diner serving Mexican chimichangas, sizzling platters and burgers.",
                4.5, "25-30 mins", "https://images.unsplash.com/photo-1550547660-d9450f859349?w=600",
                new FoodItem("tm_1", "Mexican Chimichanga", "Golden crispy tortilla stuffed with spiced beans, corn and melted cheese", 320, "https://images.unsplash.com/photo-1550547660-d9450f859349?w=500", "Continental"),
                new FoodItem("tm_2", "The Big Boss Burger", "Grilled double patty topped with onion rings & smoky chipotle mayo", 280, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500", "Burger"),
                new FoodItem("tm_3", "Sizzling Brownie with Ice Cream", "Hot cast-iron plate brownie topped with vanilla bean scoop & chocolate fudge", 210, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Dessert")
        ));

        list.add(createEntry(
                "ahmd_8", "Vishalla", "Traditional & Gujarati", "Vasna",
                "Rustic village restaurant serving mud-pot village meals under lanterns and starry open sky.",
                4.6, "35-40 mins", "https://images.unsplash.com/photo-1505253758473-96b3015f21c9?w=600",
                new FoodItem("vs_1", "Traditional Village Meal", "Bajra no Rotlo with white butter, Ringna no Olo, and jaggery", 390, "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=500", "Gujarati"),
                new FoodItem("vs_2", "Ringna No Olo (Baingan Bharta)", "Charcoal roasted spiced eggplant mash seasoned with green garlic", 190, "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500", "Gujarati")
        ));

        list.add(createEntry(
                "ahmd_9", "Bhatiyar Gali Biryani Corner", "Mughlai & Biryani", "Teen Darwaza",
                "Centuries old culinary alley serving authentic Dum Biryani, Galouti Kebabs & Haleem.",
                4.5, "25-30 mins", "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=600",
                new FoodItem("bg_1", "Hyderabadi Dum Chicken Biryani", "Aromatic long-grain basmati cooked with saffron and spices", 260, "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=500", "Biryani"),
                new FoodItem("bg_2", "Mutton Seekh Kebab (4 pcs)", "Charcoal grilled skewered minced mutton infused with ginger-garlic", 290, "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=500", "Starters"),
                new FoodItem("bg_3", "Chicken Shahi Korma", "Rich cashew and curd based mild aromatic curry", 310, "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=500", "Mughlai")
        ));

        list.add(createEntry(
                "ahmd_10", "Swati Snacks", "Chaat & Traditional", "Prahlad Nagar",
                "Famous pure vegetarian kitchen celebrating authentic Panki, Handvo, and street chaat.",
                4.7, "20-25 mins", "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=600",
                new FoodItem("ss_1", "Banana Leaf Panki", "Delicate spiced rice pancake steamed between fresh banana leaves", 190, "https://images.unsplash.com/photo-1565299585323-38d6b0865b47?w=500", "Gujarati"),
                new FoodItem("ss_2", "Baked Masala Handvo", "Savory lentil cake with sesame crust served with green chutney", 210, "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500", "Gujarati"),
                new FoodItem("ss_3", "Sev Puri Chaat", "Crispy puris topped with potatoes, trio of chutneys and nylon sev", 140, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=500", "Chaat")
        ));

        list.add(createEntry(
                "ahmd_11", "Jassi De Paranthe", "North Indian & Punjabi", "Bodakdev",
                "Famous Dhaba destination known for gigantic stuffed Punjabi tandoori paranthas & lassi.",
                4.4, "25-30 mins", "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=600",
                new FoodItem("jp_1", "Amritsari Aloo Pyaaz Parantha", "Stuffed wholewheat parantha with spiced potato, onion & dollop of butter", 180, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=500", "Punjabi"),
                new FoodItem("jp_2", "Paneer Tikka Masala", "Marinated paneer chunks grilled in tandoor and tossed in onion tomato gravy", 270, "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=500", "North Indian"),
                new FoodItem("jp_3", "Patiala Lassi (500ml)", "Giant brass glass chilled creamy lassi with thick malai topping", 110, "https://images.unsplash.com/photo-1527661591475-527312dd65f5?w=500", "Drinks")
        ));

        list.add(createEntry(
                "ahmd_12", "Mocha Café & Bar", "Cafe & Continental", "Gulbai Tekra",
                "Trendy hangout serving artisan coffees, loaded nachos, wraps & chocolate mud cake.",
                4.5, "20-25 mins", "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=600",
                new FoodItem("mo_1", "Loaded Grande Nachos", "Tortilla crisps layered with salsa, jalapenos, sour cream and melted cheese", 280, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500", "Mexican"),
                new FoodItem("mo_2", "Classic Iced Mocha Latte", "Espresso blended with dark Belgian cocoa and whole milk", 195, "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=500", "Beverages"),
                new FoodItem("mo_3", "Gooey Chocolate Mud Cake", "Rich molten chocolate cake served with dark ganache drizzle", 220, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Dessert")
        ));

        list.add(createEntry(
                "ahmd_13", "Kovallam South Indian", "South Indian", "Ellis Bridge",
                "Authentic Chennai and Mysore style breakfast with filter coffee, idlis and podi dosas.",
                4.6, "15-20 mins", "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=600",
                new FoodItem("kv_1", "Ghee Podi Masala Dosa", "Crisp paper dosa smeared with gunpowder masala and pure ghee", 175, "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=500", "South Indian"),
                new FoodItem("kv_2", "Button Ghee Sambar Idli (14 pcs)", "Mini steamed idlis drowned in hot aromatic Madras sambar", 130, "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=500", "South Indian"),
                new FoodItem("kv_3", "Madras Filter Kaapi", "Traditional chicory blend frothed in brass davarah", 65, "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=500", "Beverages")
        ));

        list.add(createEntry(
                "ahmd_14", "Simran Farm Fresh Chicken", "Fast Food & Snacks", "Vastrapur",
                "Hot grilled chicken rolls, tandoori leg pieces and crispy fried chicken wings.",
                4.3, "25-30 mins", "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=600",
                new FoodItem("sf_1", "Double Egg Chicken Kathi Roll", "Spicy chicken cubes wrapped in egg-layered paratha with mint chutney", 160, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=500", "Rolls"),
                new FoodItem("sf_2", "Crispy Fried Chicken Strips (6 pcs)", "Crunchy seasoned chicken fillets served with garlic dip", 220, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500", "Fast Food")
        ));

        list.add(createEntry(
                "ahmd_15", "Upper Crust Artisan Bakery", "Bakery & Desserts", "Vijay Cross Road",
                "Pioneering European pastry cafe known for cheesecakes, sourdough sandwiches & tarts.",
                4.6, "20-25 mins", "https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=600",
                new FoodItem("uc_1", "New York Baked Cheesecake", "Silky Philadelphia cream cheese slice with berry compote", 240, "https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=500", "Bakery"),
                new FoodItem("uc_2", "Smoked Paneer Herb Focaccia", "Marinated cottage cheese with sun-dried tomato spread in focaccia", 230, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=500", "Continental")
        ));

        list.add(createEntry(
                "ahmd_16", "Mirch Masala", "North Indian & Punjabi", "S.G. Highway",
                "Bollywood themed restaurant with retro Indian dhaba flavours and rich paneer gravies.",
                4.4, "25-30 mins", "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=600",
                new FoodItem("mm2_1", "Dal Makhani Mirch Masala", "Slow-simmered black lentils cooked overnight with cream & butter", 220, "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500", "North Indian"),
                new FoodItem("mm2_2", "Paneer Lababdar", "Soft paneer cubes in velvety cashew and tomato gravy with chopped onions", 260, "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=500", "North Indian")
        ));

        list.add(createEntry(
                "ahmd_17", "Law Garden Khau Gali", "Street Food & Chaat", "Law Garden",
                "Epic street food hub bustling with butter Pav Bhaji, Bhel Puri, and soft Kulfis.",
                4.5, "15-20 mins", "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=600",
                new FoodItem("lg2_1", "Special Amul Butter Pav Bhaji", "Spiced vegetable mash loaded with extra butter, served with soft toasted pav", 160, "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500", "Street Food"),
                new FoodItem("lg2_2", "Ahmedabadi Bhel Puri", "Puffed rice tossed with boiled potatoes, onions, sev and tangy tamarind", 70, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=500", "Chaat"),
                new FoodItem("lg2_3", "Rabdi Malai Kulfi", "Dense frozen dessert stick infused with pistachio and cardamom", 80, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Dessert")
        ));

        list.add(createEntry(
                "ahmd_18", "Tinello - Hyatt Regency", "Italian Fine Dining", "Ashram Road",
                "Authentic handcrafted thin-crust pizzas, risottos and gourmet Italian tiramisu.",
                4.8, "35-40 mins", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600",
                new FoodItem("tn_1", "Quattro Formaggi Pizza", "12-inch pizza featuring gorgonzola, parmesan, ricotta and mozzarella", 550, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500", "Pizza"),
                new FoodItem("tn_2", "Classic Italian Tiramisu", "Espresso soaked ladyfingers with whipped mascarpone cream", 320, "https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=500", "Dessert")
        ));

        list.add(createEntry(
                "ahmd_19", "Freezeland", "Fast Food & Shakes", "C.G. Road",
                "Ahmedabad youth favourite since 1989 for cheesy grilled sandwiches and thick milkshakes.",
                4.4, "20-25 mins", "https://images.unsplash.com/photo-1550547660-d9450f859349?w=600",
                new FoodItem("fl_1", "Cheese Chutney Grill Sandwich", "Crisp golden pressed sandwich with melted cheese & spicy mint chutney", 140, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=500", "Fast Food"),
                new FoodItem("fl_2", "Oreo Fantasy Thickshake", "Crushed Oreo cookies blended with chocolate ice cream", 150, "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=500", "Drinks")
        ));

        list.add(createEntry(
                "ahmd_20", "Sasumaa Gujarati Thali", "Gujarati Thali", "Prahladnagar",
                "Homely authentic Gujarati experience with warm hospitality and unlimited bowls.",
                4.5, "25-30 mins", "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=600",
                new FoodItem("sm_1", "Special Festive Gujarati Thali", "Complete thali with dry-fruit Basundi, Puran Poli and seasonal curry", 420, "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=500", "Gujarati")
        ));

        list.add(createEntry(
                "ahmd_21", "Nini's Kitchen", "Continental & Italian", "Prahlad Nagar",
                "Vibrant casual diner famous for garlic knots, pink sauce pasta, and crispy pizzas.",
                4.6, "25-30 mins", "https://images.unsplash.com/photo-1621996346565-e3d5d6281057?w=600",
                new FoodItem("nk_1", "Cheesy Garlic Herb Knots (6 pcs)", "Warm twisted dough knots topped with melted mozzarella & oregano", 210, "https://images.unsplash.com/photo-1619895092538-128341789043?w=500", "Starters"),
                new FoodItem("nk_2", "Pink Sauce Penne Pasta", "Penne tossed in delicious blend of tangy tomato & creamy white sauce", 290, "https://images.unsplash.com/photo-1621996346565-e3d5d6281057?w=500", "Italian")
        ));

        list.add(createEntry(
                "ahmd_22", "Gwalia Sweets & Fast Food", "Sweets & Chaat", "IIM Road",
                "Royal Rajasthani sweet parlor famous for Pyaaz Kachori, Raj Kachori and ghewar.",
                4.5, "15-20 mins", "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=600",
                new FoodItem("gw_1", "Jodhpur Pyaaz Kachori (2 pcs)", "Flaky crust kachori with spicy caramelized onion filling", 80, "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500", "Chaat"),
                new FoodItem("gw_2", "Shahi Raj Kachori", "Crisp bowl filled with sprouts, curd, chutneys and pomegranate", 130, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=500", "Chaat")
        ));

        list.add(createEntry(
                "ahmd_23", "Saleem Biryani", "Mughlai & Biryani", "Juhapura",
                "Renowned for fragrant long-grain chicken and mutton dum biryanis cooked over woodfire.",
                4.4, "20-25 mins", "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=600",
                new FoodItem("sb_1", "Special Chicken Dum Biryani", "Fragrant saffron rice with succulent marinated chicken", 240, "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=500", "Biryani"),
                new FoodItem("sb_2", "Chicken Fry Crispy", "Spicy battered deep fried chicken pieces with onion rings", 190, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500", "Starters")
        ));

        list.add(createEntry(
                "ahmd_24", "Chaitanya Parantha", "North Indian", "Vastrapur Lake",
                "Cozy North Indian stop serving piping hot tava and tandoori paranthas with fresh curd.",
                4.3, "20-25 mins", "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=600",
                new FoodItem("cp_1", "Paneer Cheese Parantha", "Stuffed with grated cottage cheese and green chillies", 170, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=500", "North Indian")
        ));

        list.add(createEntry(
                "ahmd_25", "Karnavati Dabeli Centre", "Street Food", "Maninagar",
                "Home of authentic Kutchi Dabeli with spicy potato masala, peanuts and pomegranate.",
                4.6, "15-20 mins", "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=600",
                new FoodItem("kd_1", "Special Butter Cheese Dabeli (2 pcs)", "Soft pav stuffed with masala potato, roasted masala peanuts, cheese & sev", 90, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=500", "Street Food"),
                new FoodItem("kd_2", "Oil Roast Masala Dabeli (2 pcs)", "Classic toasted dabeli with sweet-spicy garlic chutney", 60, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=500", "Street Food")
        ));

        list.add(createEntry(
                "ahmd_26", "Café Mocha Bodakdev", "Cafe & Continental", "Sindhu Bhavan Road",
                "Chic hangout for gourmet cold brews, thin-crust woodfire pizzas and avocado toast.",
                4.6, "25-30 mins", "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=600",
                new FoodItem("cb_1", "Avocado Sourdough Toast", "Hass avocado mash on toasted sourdough topped with feta cheese", 290, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=500", "Continental"),
                new FoodItem("cb_2", "Hazelnut Cold Coffee Frappe", "Chilled roasted espresso with hazelnut cream syrup", 210, "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=500", "Drinks")
        ));

        list.add(createEntry(
                "ahmd_27", "Atithi Dining Hall", "Gujarati Thali", "Bodakdev",
                "Popular for warm hospitality, unlimited Gujarati thali, and festive special desserts.",
                4.5, "25-30 mins", "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600",
                new FoodItem("at_1", "Authentic Kathiyawadi Thali", "Sev Tameta, Baingan Bharta, Bajra Rotla, Garlic Chutney, Chaas", 380, "https://images.unsplash.com/photo-1626777552726-4a6b54c97e46?w=500", "Gujarati")
        ));

        list.add(createEntry(
                "ahmd_28", "The Red Bistro", "Continental & Italian", "Vastrapur",
                "Cosy Italian and Continental eatery famous for creamy risottos and baked lasagne.",
                4.4, "25-30 mins", "https://images.unsplash.com/photo-1621996346565-e3d5d6281057?w=600",
                new FoodItem("rb_1", "Baked Vegetable Lasagne", "Layers of pasta sheet, zucchini, bell peppers and parmesan béchamel", 310, "https://images.unsplash.com/photo-1621996346565-e3d5d6281057?w=500", "Italian"),
                new FoodItem("rb_2", "Cheese Fondue Platter", "Warm Swiss cheese dip pot served with croutons and grilled veggies", 390, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500", "Continental")
        ));

        list.add(createEntry(
                "ahmd_29", "Shambhu's Coffee Bar", "Coffee & Snacks", "Vastrapur",
                "The classic Ahmedabad meeting point for cold cocoa, grilled maskabun & coffee.",
                4.5, "15-20 mins", "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=600",
                new FoodItem("sc_1", "Thick Cold Cocoa with Ice Cream", "Famous thick chocolate drink topped with rich chocolate scoop", 110, "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=500", "Drinks"),
                new FoodItem("sc_2", "Butter Maska Bun", "Soft sweet bun slit and loaded with salted Amul butter", 60, "https://images.unsplash.com/photo-1509722747041-616f39b57569?w=500", "Snacks")
        ));

        list.add(createEntry(
                "ahmd_30", "Sankalp Restaurant", "South Indian", "C.G. Road",
                "World record holder for longest dosas; serving 30+ varieties of authentic dosas.",
                4.7, "20-25 mins", "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=600",
                new FoodItem("sk_1", "Military Ghee Roast Dosa", "Spicy red chili & garlic paste smeared thin crispy dosa", 210, "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=500", "South Indian"),
                new FoodItem("sk_2", "Rava Onion Masala Dosa", "Cracked semolina crisp crepe stuffed with tempered potatoes", 195, "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=500", "South Indian")
        ));

        list.add(createEntry(
                "ahmd_31", "Havmor Restaurant", "North Indian & Desserts", "Navrangpura",
                "Ahmedabad legacy heritage dining serving legendary Chana Puri and ice creams since 1944.",
                4.6, "20-25 mins", "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600",
                new FoodItem("hv_1", "Signature Chana Bhatura", "Spiced dark Punjabi chickpeas served with two hot fluffy bhaturas", 220, "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500", "North Indian"),
                new FoodItem("hv_2", "Taj Mahal Ice Cream Sundae", "Three scoops of royal ice cream with nuts, fruit and chocolate fudge", 180, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Dessert")
        ));

        list.add(createEntry(
                "ahmd_32", "Purohit Thali", "Gujarati & Rajasthani", "Ellis Bridge",
                "Famous pure vegetarian kitchen serving traditional ghee rotis, dal baati and churma.",
                4.4, "25-30 mins", "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600",
                new FoodItem("pt_1", "Dal Baati Churma Platter", "Crisp baked baatis drowned in ghee, spicy panchmel dal and sweet churma", 340, "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500", "Rajasthani")
        ));

        list.add(createEntry(
                "ahmd_33", "Madhavrao Kathiyawadi", "Kathiyawadi", "Sindhu Bhavan Road",
                "Spicy rural Gujarat comfort food: Sev Tameta, Ringna Olo and fresh buttermilk.",
                4.5, "25-30 mins", "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600",
                new FoodItem("mr_1", "Kathiyawadi Sev Tameta Nu Shaak", "Spicy tangy tomato gravy topped with crispy ratlami sev", 180, "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500", "Kathiyawadi"),
                new FoodItem("mr_2", "Hot Bajra Rotla with Ghee & Gur", "Thick wholesome pearl millet bread with organic jaggery", 70, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=500", "Breads")
        ));

        list.add(createEntry(
                "ahmd_34", "Caffè Bella", "Italian & Continental", "Sindhu Bhavan",
                "Artisan woodfired Neapolitan pizzas, handmade ravioli and cold brewed mocktails.",
                4.7, "30-35 mins", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600",
                new FoodItem("cb2_1", "Margherita Burrata Pizza", "San Marzano tomato base topped with fresh creamy whole Burrata ball", 480, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500", "Pizza"),
                new FoodItem("cb2_2", "Spinach & Ricotta Ravioli", "Handmade pasta pockets tossed in butter sage sauce", 360, "https://images.unsplash.com/photo-1621996346565-e3d5d6281057?w=500", "Italian")
        ));

        list.add(createEntry(
                "ahmd_35", "Fozzie's Pizzaiolo", "Artisan Pizza", "Bodakdev",
                "Handcrafted deep-dish Chicago and Neapolitan style sourdough pizzas with exotic toppings.",
                4.6, "25-30 mins", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600",
                new FoodItem("fz_1", "Smoky BBQ Paneer Sourdough Pizza", "Smoked paneer, caramelized onions, mozzarella and jalapeño", 380, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500", "Pizza"),
                new FoodItem("fz_2", "Cheesy Jalapeno Poppers (6 pcs)", "Crisp golden bites oozing with melted cheese and spicy peppers", 190, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500", "Starters")
        ));

        list.add(createEntry(
                "ahmd_36", "The Chocolate Room", "Desserts & Shakes", "Vastrapur",
                "Heaven for chocolate lovers: chocolate fondues, Belgian waffles and chocolate pizzas.",
                4.5, "20-25 mins", "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=600",
                new FoodItem("cr_1", "Belgian Dark Chocolate Waffle", "Warm crisp waffle topped with molten Belgian chocolate & vanilla scoop", 220, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Dessert"),
                new FoodItem("cr_2", "Chocolate Bomb with Hot Fudge", "Chocolate sphere melting under warm dark fudge sauce", 240, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Dessert")
        ));

        list.add(createEntry(
                "ahmd_37", "Jay Bhavani Vadapav", "Street Food", "Satellite",
                "Gujarat's favorite jumbo vadapav and butter dabeli joint with signature red garlic chutney.",
                4.6, "15-20 mins", "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=600",
                new FoodItem("jb_1", "Cheese Butter Grilled Vadapav", "Crispy spiced potato vada inside grilled buttery bun with cheese slice", 65, "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=500", "Street Food"),
                new FoodItem("jb_2", "Masala Cheese French Fries", "Spicy tossed fries with melted cheese drizzle", 110, "https://images.unsplash.com/photo-1576107232684-1279f3908594?w=500", "Snacks")
        ));

        list.add(createEntry(
                "ahmd_38", "Sankalp Gourmet South Indian", "South Indian", "Sindhu Bhavan",
                "Upscale contemporary South Indian kitchen with Malabar parottas, ghee podi and idlis.",
                4.6, "25-30 mins", "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=600",
                new FoodItem("sk2_1", "Cheese Corn Spring Dosa", "Rolled crisp dosa filled with spicy vegetable and corn stuffing", 240, "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=500", "South Indian"),
                new FoodItem("sk2_2", "Malabar Parotta with Veg Kurma", "Flaky layered flatbread served with coconut milk curry", 210, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=500", "South Indian")
        ));

        list.add(createEntry(
                "ahmd_39", "Kankaria Lake Food Plaza", "Street Food & Chaat", "Maninagar",
                "Lakeside food hub offering spicy tawa pulav, dahi puri and hot roasted bhutta.",
                4.4, "20-25 mins", "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600",
                new FoodItem("kl_1", "Special Mumbai Tawa Pulao", "Basmati rice tossed on giant tawa with pav bhaji butter masala", 150, "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500", "Street Food"),
                new FoodItem("kl_2", "Sev Batata Dahi Puri (6 pcs)", "Crispy puris topped with curd, sweet tamarind and mountain of sev", 90, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=500", "Chaat")
        ));

        list.add(createEntry(
                "ahmd_40", "Under The Neem Tree", "North Indian Fine Dining", "Science City",
                "Serene garden restaurant serving charcoal kebabs, Awadhi biryani and saffron paneer.",
                4.7, "35-40 mins", "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=600",
                new FoodItem("un_1", "Zafrani Paneer Tikka", "Charcoal roasted cottage cheese marinated in cream and Kashmiri saffron", 320, "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=500", "Starters"),
                new FoodItem("un_2", "Subz Dum Biryani Handi", "Slow clay-pot cooked basmati rice with minted herbs & raita", 280, "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=500", "Biryani")
        ));

        list.add(createEntry(
                "ahmd_41", "Shakti The Sandwich Shop", "Sandwich & Fast Food", "Prahlad Nagar",
                "Local sandwich institution with 40+ cold and grilled sandwich creations with cheese.",
                4.5, "15-20 mins", "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=600",
                new FoodItem("sh_1", "Junglee Cheese Grill Sandwich", "Signature spicy capsicum, corn and onion grilled sandwich", 160, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=500", "Fast Food"),
                new FoodItem("sh_2", "Aloo Masala Toast Sandwich", "Seasoned potatoes with green chutney and toasted crunch", 100, "https://images.unsplash.com/photo-1528735602780-2552fd46c7af?w=500", "Fast Food")
        ));

        list.add(createEntry(
                "ahmd_42", "Kailash Parbat Hindu Sweets", "Chaat & Punjabi", "C.G. Road",
                "Sindhi and Mumbai street chaat master known for Ragda Pattice and Chole Bhature.",
                4.5, "20-25 mins", "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=600",
                new FoodItem("kp_1", "Ragda Pattice (2 pcs)", "Potato patties topped with dried white peas gravy, curd and sweet saunth", 130, "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500", "Chaat"),
                new FoodItem("kp_2", "Sindhi Koki with Curd & Pickle", "Thick spiced wholewheat flatbread roasted with onion & pomegranate seeds", 140, "https://images.unsplash.com/photo-1626082927389-6cd097cdc6ec?w=500", "Snacks")
        ));

        list.add(createEntry(
                "ahmd_43", "The Jungle Bhookh", "Family Dining", "S.G. Highway",
                "Theme based family dining serving North Indian gravies, sizzling sizzlers and mocktails.",
                4.3, "25-30 mins", "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600",
                new FoodItem("jb2_1", "Paneer Peshawari Gravy", "Rich crushed black pepper and cashew gravy with soft paneer cubes", 260, "https://images.unsplash.com/photo-1631452180519-c014fe946bc7?w=500", "North Indian"),
                new FoodItem("jb2_2", "Jungle Special Sizzler", "French fries, rice, grilled paneer steak & brown pepper sauce on sizzler plate", 340, "https://images.unsplash.com/photo-1550547660-d9450f859349?w=500", "Continental")
        ));

        list.add(createEntry(
                "ahmd_44", "Pizzawala's", "Pizza & Italian", "Ambawadi",
                "Authentic Gujarati fusion pizzas with double cheese burst and zesty herb toppings.",
                4.4, "20-25 mins", "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600",
                new FoodItem("pw_1", "Double Cheese Volcano Pizza", "Overloaded cheese lava center surrounded by crunchy crust", 299, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500", "Pizza"),
                new FoodItem("pw_2", "Peri Peri Cheese Garlic Bread", "Pull-apart garlic bread loaded with spicy peri-peri dust", 130, "https://images.unsplash.com/photo-1619895092538-128341789043?w=500", "Starters")
        ));

        list.add(createEntry(
                "ahmd_45", "Dhoom Dhaam Kathiyawadi", "Kathiyawadi", "Naroda",
                "Authentic Saurashtra village food with Baingan Olo, Ringan Bharta, and Kadhi Khichdi.",
                4.5, "25-30 mins", "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600",
                new FoodItem("dd_1", "Rajwadi Dhokli Nu Shaak", "Traditional besan dumplings simmered in tangy buttermilk gravy", 170, "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=500", "Kathiyawadi"),
                new FoodItem("dd_2", "Masala Vaghareli Khichdi", "Comforting yellow moong and rice tempered with cumin and ghee", 150, "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=500", "Gujarati")
        ));

        list.add(createEntry(
                "ahmd_46", "Mad By Tomato's", "American & Mexican", "Sindhu Bhavan",
                "Retro funky diner serving Chicago thick crust pizzas, buffalo wings & shakes.",
                4.6, "30-35 mins", "https://images.unsplash.com/photo-1550547660-d9450f859349?w=600",
                new FoodItem("mbt_1", "Buffalo Spiced Chicken Wings (6 pcs)", "Crispy wings coated in hot tangy Louisiana glaze with blue cheese dip", 280, "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=500", "Fast Food"),
                new FoodItem("mbt_2", "Truffle Mushroom Burger", "Herb roasted wild mushrooms with melted gouda and truffle mayo", 310, "https://images.unsplash.com/photo-1550547660-d9450f859349?w=500", "Burger")
        ));

        list.add(createEntry(
                "ahmd_47", "Iscon Gathiya", "Farsan & Breakfast", "Iscon Cross Road",
                "Ahmedabad's legendary hot Bhavnagri Gathiya, Kadhi, Papaya Sambharo and Jalebi.",
                4.7, "15-20 mins", "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=600",
                new FoodItem("ig_1", "Fresh Bhavnagri Gathiya (250g)", "Spiced savory chickpea flour snack served with hot sweet Kadhi", 85, "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=500", "Farsan"),
                new FoodItem("ig_2", "Pure Ghee Jalebi (250g)", "Crispy saffron sugar syrup soaked coils fried in pure desi ghee", 140, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Dessert")
        ));

        list.add(createEntry(
                "ahmd_48", "Saleem’s Nihari & Mughlai", "Mughlai", "Rakhial",
                "Famous slow-cooked overnight Nihari, Tawa Mutton and soft roomali rotis.",
                4.5, "25-30 mins", "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=600",
                new FoodItem("sn_1", "Slow Cooked Mutton Nihari", "Slow-stewed mutton shank in rich aromatic bone marrow gravy", 360, "https://images.unsplash.com/photo-1589302168068-964664d93dc0?w=500", "Mughlai"),
                new FoodItem("sn_2", "Soft Roomali Roti (2 pcs)", "Paper thin handkerchief flatbread baked over inverted kadai", 50, "https://images.unsplash.com/photo-1621996346565-e3d5d6281057?w=500", "Breads")
        ));

        list.add(createEntry(
                "ahmd_49", "The Green House - House of MG", "Heritage Snacks & Drinks", "Lal Darwaja",
                "Courtyard garden heritage cafe serving seasonal hand-churned ice creams, khaman and teas.",
                4.7, "20-25 mins", "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=600",
                new FoodItem("gh_1", "Fresh Seasonal Sitaphal Ice Cream", "Hand-churned artisanal custard apple ice cream with natural fruit pulp", 160, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Dessert"),
                new FoodItem("gh_2", "Spiced Lemon Iced Chai", "Refreshing Assam black tea infused with lemon, mint and spices", 95, "https://images.unsplash.com/photo-1517701550927-30cf4ba1dba5?w=500", "Drinks")
        ));

        list.add(createEntry(
                "ahmd_50", "BBQ Nation Ahmedabad", "Barbeque & Grill", "Thaltej",
                "Celebrated live grill over table skewers, crispy corn, chicken tikkas and kulfis.",
                4.6, "30-35 mins", "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=600",
                new FoodItem("bbq_1", "Crispy Cajun Spiced Corn", "Golden fried sweet corn kernels tossed in tangy Cajun seasoning", 210, "https://images.unsplash.com/photo-1576107232684-1279f3908594?w=500", "Starters"),
                new FoodItem("bbq_2", "Tandoori Murgh Tikka Box", "Boneless chicken skewers marinated in yogurt, roasted cumin & fenugreek", 320, "https://images.unsplash.com/photo-1599488615731-7e5c2823ff28?w=500", "Starters"),
                new FoodItem("bbq_3", "Assorted Kulfi Nation Platter", "Trio of Paan, Mango and Malai kulfis with falooda & rabdi", 160, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Dessert")
        ));

        list.add(createEntry(
                "ahmd_51", "Bikanervala Ahmedabad", "Chaat & North Indian", "Satellite",
                "North Indian specialties with Raj Kachori, Chhole Kulche, and traditional motichoor ladoos.",
                4.5, "20-25 mins", "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=600",
                new FoodItem("bv_1", "Amritsari Kulcha with Pindi Chana", "Crushed layered baked bread with tangy chickpea masala", 210, "https://images.unsplash.com/photo-1601050690597-df0568f70950?w=500", "North Indian"),
                new FoodItem("bv_2", "Moong Dal Halwa (150g)", "Rich golden halwa roasted with pure desi ghee and dry fruits", 130, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Dessert")
        ));

        list.add(createEntry(
                "ahmd_52", "Huber & Holly", "Gourmet Ice Cream & Pizza", "Bodakdev",
                "Luxury freshly-churned ice cream parlour famous for 16-flavor Mighty Midas gold-leaf cone.",
                4.7, "20-25 mins", "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=600",
                new FoodItem("hh_1", "Belgian Dark Chocolate Footlong Pastry", "Intense 70% cocoa pastry slice topped with gold leaf", 280, "https://images.unsplash.com/photo-1541658016709-82535e94bc69?w=500", "Dessert"),
                new FoodItem("hh_2", "Woodfired Truffle Mushroom Pizza", "Thin crust gourmet pizza with wild mushrooms and truffle drizzle", 420, "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=500", "Pizza")
        ));

        return list;
    }

    public static List<FoodItem> getFoodsForRestaurant(String restaurantId) {
        if (restaurantId == null) return Collections.emptyList();
        for (AhmedabadRestaurantEntry entry : getAhmedabadRestaurants()) {
            if (restaurantId.equals(entry.restaurant.getId())) {
                return entry.foods;
            }
        }
        return Collections.emptyList();
    }

    private static AhmedabadRestaurantEntry createEntry(
            String id, String name, String category, String area,
            String desc, double rating, String deliveryTime, String imageUrl,
            FoodItem... dishes) {
        Restaurant restaurant = new Restaurant(id, name, category, area, desc, rating, deliveryTime, imageUrl);
        List<FoodItem> foodList = new ArrayList<>();
        Collections.addAll(foodList, dishes);
        return new AhmedabadRestaurantEntry(restaurant, foodList);
    }
}
