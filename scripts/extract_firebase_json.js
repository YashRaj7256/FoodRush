const fs = require('fs');
const path = require('path');

const seederPath = path.join(__dirname, '..', 'app', 'src', 'main', 'java', 'com', 'foodrush', 'database', 'AhmedabadDataSeeder.java');
const content = fs.readFileSync(seederPath, 'utf8');

const restaurants = {};
const entries = content.split('list.add(createEntry(');

for (let i = 1; i < entries.length; i++) {
    const block = entries[i];
    const headerMatch = block.match(/"([^"]+)",\s*"([^"]+)",\s*"([^"]+)",\s*"([^"]+)",\s*"([^"]+)",\s*([\d\.]+),\s*"([^"]+)",\s*"([^"]+)"/);
    if (!headerMatch) continue;
    const [_, id, name, category, area, desc, rating, deliveryTime, imageUrl] = headerMatch;

    const foods = {};
    const foodRegex = /new FoodItem\("([^"]+)",\s*"([^"]+)",\s*"([^"]+)",\s*(\d+),\s*"([^"]+)",\s*"([^"]+)"\)/g;
    let fMatch;
    while ((fMatch = foodRegex.exec(block)) !== null) {
        const [__, fid, fname, fdesc, price, fimg, fcat] = fMatch;
        const isNonVeg = /chicken|mutton|fish|meat|egg|prawn|beef|bacon/i.test(fname);
        foods[fid] = {
            id: fid,
            name: fname,
            description: fdesc,
            price: Number(price),
            imageUrl: fimg,
            category: fcat,
            available: true,
            veg: !isNonVeg
        };
    }

    restaurants[id] = {
        id: id,
        name: name,
        category: category,
        area: area,
        description: desc,
        rating: Number(rating),
        deliveryTime: deliveryTime,
        imageUrl: imageUrl,
        foods: foods
    };
}

const outPath = path.join(__dirname, 'ahmedabad_restaurants.json');
fs.writeFileSync(outPath, JSON.stringify(restaurants, null, 2), 'utf8');

console.log(`Extracted ${Object.keys(restaurants).length} restaurants.`);
console.log(`Saved JSON to: ${outPath}`);
