const URL = 'http://localhost:8080/data';

let wproducts = [];

/**
 * document
 *      - gives access to html elements
 * 
 * event listeners are how we respod to events in the html
 */

document.addEventListener('DOMContentLoaded', () => {

    /**
     * DOM - Document Object Model 
     * 
     * DOMContentLoaded is an event that fires when the DOM is loaded
     */

    /**
     * AJAX - Asynchronous JavaScript and XML
     *      pirmary object of AJAX is XMLHttpRequest (XHR)
     */
    
    let xhr = new XMLHttpRequest(); //creates a new XHR object (puts it in to state 0 - unsent)

    /**
     * 
     *  5 stages (ready states):
     *      0 - unsent
     *      1 - opened 
     *      2 - headers recieved
     *      3 - loading
     *      4 - done    --> main state we care about
     * 
     */

    xhr.onreadystatechange = () => {

        /**
         * strict equality (===) and regular equality (==) in JS
         *      strict equality checks vaules and datatype
         *      regular qeuality only checks values
         * 
         *      ex: 4 === '4' returns false but 4 =='4' returns true
         */

        if(xhr.readyState === 4) {
            //at readyState 4, we hvae our response from the server

            //JSON.parse() parses the JSON into js objects
            let products = JSON.parse(xhr.responseText);
            populateWareHouse(products)
            //adds movies to table
            console.log(products);
        }
    }

    //sets the request method and the url to send the request to (state changed to 1 - opened)
    xhr.open('GET', URL);

    xhr.send();

});

document.getElementById('productSearch').addEventListener('submit', (event) => {
    event.preventDefault();
    let inputData = new FormData(document.getElementById('productSearch'));

    // use .get() to retrieve a field from form data and pass in the NAME attribute from the <input> tag
   let keyWord = inputData.get("keyWord")
    console.log(keyWord);
    doGetRequest(keyWord)
    closePopup();

});
document.getElementById('newProduct').addEventListener('submit', (event) => {
    event.preventDefault();
    let inputData = new FormData(document.getElementById('newProduct'));

    // use .get() to retrieve a field from form data and pass in the NAME attribute from the <input> tag
    let newProduct = {
        name : inputData.get('name'),
        category : inputData.get('category'),
        price : inputData.get('price'),
        quantity : inputData.get('quantity'),
        rating : inputData.get('rating')
        
    }
    console.log(newProduct);
    doPostRequest(newProduct)

});
document.getElementById('popupForm').addEventListener('submit', (event) => {
    event.preventDefault();
    console.log("here")
    let inputData = new FormData(document.getElementById('popupForm'));
    
    // use .get() to retrieve a field from form data and pass in the NAME attribute from the <input> tag
    let updatedProduct = {
        id: inputData.get('pid'),
        name : inputData.get('pname'),
        category : inputData.get('pcategory'),
        price : inputData.get('pprice'),
        quantity : inputData.get('pquantity'),
        rating : inputData.get('prating'),
        sn: inputData.get("psn")
    }
    let test  = inputData.get('pid')
    let test3= inputData.get('pcategory')

    console.log(test);
    doUpdateRequest(updatedProduct)

});
// document.getElementById('getProds').addEventLisenter('submit',(event)=>{
//     let inputData = Document.getElementById("warehouseo");
//     console.log(inputData);


// });
async function doPostRequest(newProduct) {

    console.log(newProduct);
    let returnedData = await fetch(URL + '/newProduct', {
        method : 'POST',
        headers : {
            'Content-Type' : 'application/json' //makes sure your server is expecting to recieve JSON in the body
        },
        body : JSON.stringify(newProduct)  //turns js object into json
    });

    //.json() to deserialize the JSON back into js object - this ALSO returns a promise
    let productJson = await returnedData.json();
    wproducts.push(productJson);
    if(productJson==[])
    {
        alert("WareHouse is at Capacity")
    }
   else
    {
        console.log(productJson)
        populateProducts(wproducts)
        successButton();
    }

    //add movie to table
    // addMovieToTable(movieJson);

    // //reset the form
    // document.getElementById('new-movie-form').reset();
}


async function doUpdateRequest(updatedProduct) {
   
   

    
    
    let returnedData = await fetch(URL + '/products/'+ updatedProduct.id + '/updateProduct', {
        method : 'POST',
        headers : {
            'Content-Type' : 'application/json' //makes sure your server is expecting to recieve JSON in the body
        },
        body : JSON.stringify(updatedProduct) //turns js object into json
    });
    //.json() to deserialize the JSON back into js object - this ALSO returns a promise
    console.log()

    let productJson = await returnedData.json();

    if(updatedProduct.id==null)
    {
        objIndex = wproducts.findIndex(obj => obj.sn == updatedProduct.sn);
    wproducts[objIndex].id = updatedProduct.id
    wproducts[objIndex].category = updatedProduct.category
    wproducts[objIndex].name = updatedProduct.name
    wproducts[objIndex].price = updatedProduct.price
    wproducts[objIndex].rating = updatedProduct.rating
    wproducts[objIndex].quantity = updatedProduct.quantity
    }
    else
    {
        objIndex = wproducts.findIndex(obj => obj.id == updatedProduct.id);
        wproducts[objIndex].id = updatedProduct.id
        wproducts[objIndex].category = updatedProduct.category
        wproducts[objIndex].name = updatedProduct.name
        wproducts[objIndex].price = updatedProduct.price
        wproducts[objIndex].rating = updatedProduct.rating
        wproducts[objIndex].quantity = updatedProduct.quantity

    }
   



    if(returnedData)
    {
        
        
        serveResults(wproducts) 
        populateProducts(wproducts)   
        closePopup()    // window.location.reload() 
    }

    //add movie to table
    // addMovieToTable(movieJson);

    // //reset the form
    // document.getElementById('new-movie-form').reset();
}
async function doGetRequest(keyWord) {

    console.log(keyWord);
    let returnedData = await fetch(URL +'/searchProduct/'+keyWord, {
        method : 'GET',
       
    });
    

    //.json() to deserialize the JSON back into js object - this ALSO returns a promise
    let productJson =  await returnedData.json();
    if(productJson)
    {
        console.log(productJson)
        serveResults(productJson);
    }
    if(productJson.length==0)
    {
        
        searchSuccessButton();
    }
    
    
    //add movie to table
    // addMovieToTable(movieJson);

    // //reset the form
    // document.getElementById('new-movie-form').reset();
}
async function doGetProdRequest(keyWord) {

    console.log(keyWord);
    let returnedData = await fetch(URL +'/products/'+keyWord, {
        method : 'GET',
        headers : {
            'Content-Type' : 'application/json' //makes sure your server is expecting to recieve JSON in the body
        }
       
    });
    

    //.json() to deserialize the JSON back into js object - this ALSO returns a promise
    wproducts =  await returnedData.json();
    console.log(wproducts)
    populateProducts(wproducts)
    
    //add movie to table
    // addMovieToTable(movieJson);

    // //reset the form
    // document.getElementById('new-movie-form').reset();
}
function populateWareHouse(data)
{
    let wareHouse = document.getElementById("warehouse")
    var options=`
    <label>Choose a Warehouse</label> 
	<select name="warehouse" id="warehouseo"  th:field="*{rating}" >`;
    for (var i = 0; i < data.length; i++) {
        options += '<option value="' + data[i].id+ '">' + data[i].name + ' ('+ data[i].capacity+')'+'</option>';
     }
   options+= '</select>';
   wareHouse.innerHTML=options
}
function populateProducts(data)
{
    console.log(data);
    const tableBody = document.getElementById('allProds');
    let obj = data;
    tableBody.innerHTML=""
    data.forEach(item => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${item.category}</td>
            <td>${item.name}</td>
            <td>${item.rating}</td>
            <td>${item.quantity}</td>
            <td>${item.price}</td>
            <td>${item.sn}</td>
            <td width="10%">
            <a href="#" onclick="testfunc({ name: '${item.name}', category: '${item.category}', price: '${item.price}', quantity: '${item.quantity}' , rating: '${item.rating}', sn: '${item.sn}', id: '${item.id}'},${item.id})"">Edit</a> |
            <button href="#" onclick="deleteProduct({ name: '${item.name}', category: '${item.category}', price: '${item.price}', quantity: '${item.quantity}' , rating: '${item.rating}', sn: '${item.sn}', id: '${item.id}'},'${obj[item]}')"">delete</button   >
        </td>
        `;
        tableBody.appendChild(row);
    });
    // console.log(row);
}

function successButton()
{
    const b = document.getElementById("successButton")
    b.innerHTML = `<div class="alert alert-success alert-dismissible fade show p-2 mt-1 mb-3"
    role="alert"> 
    <strong>Product Added successfully! 
        <button type="button" class="btn-close" data-bs-dismiss="alert"
            aria-label="Close"></button> 
    </strong> 
</div>`;

}
function searchSuccessButton()
{
    const b = document.getElementById("cantfind")
    b.innerHTML = `<div class="alert alert-warning alert-dismissible fade show" role="alert"> 
    Product Details Not Found! 
        <button type="button" class="btn-close" data-bs-dismiss="alert"
            aria-label="Close"></button> 
</div> `;

}
function serveResults(product)
{
    let obj = product
    const tableBody = document.getElementById('productFound');
    tableBody.innerHTML="";
    product.forEach(item => {
        const row = document.createElement("tr");
        row.innerHTML = `
        <td th:text="">${item.category}</td> 
        <td th:text="">${item.name}</td> 
        <td text="">${item.rating}</td> 
        <td th:text="">${item.quantity}</td> 
        <td th:text="">${item.price}</td> 
        <td th:text="">${item.sn}</td> 
        <td width="10%">
            <a href="#" onclick="testfunc({ name: '${item.name}', category: '${item.category}', price: '${item.price}', quantity: '${item.quantity}' , rating: '${item.rating}', sn: '${item.sn}', id: '${item.id}'},${item.id})"">Edit</a> |
            <button href="#" onclick="deleteProduct({ name: '${item.name}', category: '${item.category}', price: '${item.price}', quantity: '${item.quantity}' , rating: '${item.rating}', sn: '${item.sn}', id: '${item.id}'},'${obj[item]}')"">delete</button   >

        </td>

    </tr>  `;
        tableBody.appendChild(row);
    });
    

}

function testfunc(obj,id)
{

    console.log(obj.id);
    var id = obj.id;
    document.getElementById("popup").style.display = "block";
    document.getElementById("overlay").style.display = "block";
    var outputDiv = document.getElementById("popupForm");
    var html = outputDiv.innerHTML;
    var jsonString = JSON.stringify(obj);

    // Replace template literals with object values
    html = html.replace(/\${(.*?)}/g, function(match, p1) {
      return obj[p1.trim()];
    });

    let temp=document.getElementById("namee");
    temp.innerHTML = `${obj.name}`;
    console.log(obj.id);
    outputDiv.innerHTML = `
    <form id="popupForm">
    <label>product Category</label> 
    <input type="text" name="pid" style="display: none;" value='${obj.id}'>
    <input type="text" name="psn" style="display: none;" value='${obj.sn}'>
    <select name="pcategory" required> 
        <option  text='${obj.category}' >${obj.category}</option> 
        <option value="Electronics">Electronics</option> 
        <option value="Fashion">Fashion</option> 
        <option value="Furniture">Furniture</option> 
        <option value="Home&Kitchen">Home & Kitchen</option> 
        <option value="Grocery">Grocery</option> 
    </select> <br>
    <label>product Name</label> 
        <input name="pname" type="text" id="namee" value='${obj.name}'
            placeholder="name of the product"><br>
            <label> Rating</label> 
            <select name="prating" value='${obj.rating}' required> 
                <option value="1">1</option> 
                <option value="2">2</option> 
                <option value="3">3</option> 
                <option value="4">4</option> 
                <option value="5">5</option> 
            </select>
            <label>product Quantity</label> 
            <input name="pquantity" type="number" value='${obj.quantity}' 
            value='${obj.quantity}'><br>
            <label>product Price</label> 
            <input name="pprice" type="text"   
            value='${obj.price}'> 
    <button type="submit" >Submit</button>
    <button onclick="closePopup()" type="POST">close</button>
</form>
`;
    var num = document.getElementById("popupForm");
        document.getElementById("namee").innerHTML=jsonString.name;
     console.log(outputDiv.innerHTML);     
    
}

function closePopup() {
    // document.getElementById("popup").innerHTML="";
    document.getElementById("popup").style.display = "none";
    document.getElementById("overlay").style.display = "none";
    
    
    
  }
  function showContent()
  {
    const output = $('#warehouseo').val();
            $('.output').text(output);
    var inputField = document.getElementById("warehouseo");
    var lockedContent = document.getElementById("lockedContent");
    var t = document.getElementById("warehouseo");
    console.log(output);
    // Check if input field has a value
    if (inputField.value.trim() !== "") {
      // If input has a value, unlock the content
      lockedContent.style.display = "block";
    } else {
      // If input is empty, keep the content locked
      lockedContent.style.display = "none";
    }
    doGetProdRequest(output)
  }
async function deleteProduct(obj,myData)
{
    let useData = JSON.stringify(myData)

    
    console.log(wproducts);
   fetch(URL + '/product', {
        method : 'DELETE',
        headers : {
            'Content-Type' : 'application/json',
        },
        body : JSON.stringify(obj)
    })
    .then((data) => {
        
        //delete request returns no-content so there's no need to deserialize the response and wait for a promise
        // just need to check that the response we get back is a 204 - No Content and then we can delete it
        if(data.status === 204) {
            const filteredProducts = wproducts.filter((item) => item.id != obj.id);
            wproducts = filteredProducts
           populateProducts(wproducts)
           serveResults(wproducts);
        }
    })
    .catch((error) => {
        console.error(error);
    })

    
}   