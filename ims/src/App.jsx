import React, { useMemo, useState } from "react";
import axios from "axios";

function App() {
  const BASE_URL = "http://localhost:8080";

  const [page, setPage] = useState("login");

  const [emailId, setEmailId] = useState("");
  const [password, setPassword] = useState("");
  const [userName, setUserName] = useState("");
  const [phoneNo, setPhoneNo] = useState("");
  const [role, setRole] = useState("CUSTOMER");
  const [products, setProducts] = useState([]);
  const [productsLoading, setProductsLoading] = useState(false);
  const [productsError, setProductsError] = useState("");
  const [searchText, setSearchText] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("ALL");
  const [showProductForm, setShowProductForm] = useState(false);
  const [showAddressForm, setShowAddressForm] = useState(false);
  const [editingProduct, setEditingProduct] = useState(null);
  const [productForm, setProductForm] = useState({
    productName: "",
    description: "",
    price: "",
    categoryType: "",
    stockQuantity: "",
  });
  const getSavedAddress = () => {
    try {
      return JSON.parse(localStorage.getItem("customerAddress") || "{}");
    } catch (error) {
      return {};
    }
  };
  const savedAddress = getSavedAddress();
  const [addressId, setAddressId] = useState(savedAddress.addressId || savedAddress.id || "");
  const [address, setAddress] = useState({
    houseNo: savedAddress.houseNo || "",
    street: savedAddress.street || "",
    city: savedAddress.city || "",
    state: savedAddress.state || "",
    pinCode: savedAddress.pinCode || savedAddress.pincode || "",
    country: savedAddress.country || "India",
  });
  const [showOrders, setShowOrders] = useState(false);
  const [orderItems, setOrderItems] = useState([]);
  const [ordersLoading, setOrdersLoading] = useState(false);
  const [selectedProduct, setSelectedProduct] = useState(null);
  const [orderQuantity, setOrderQuantity] = useState(1);
  const [placedOrder, setPlacedOrder] = useState(null);
  const [paymentMode, setPaymentMode] = useState("UPI");
  const [users, setUsers] = useState([]);
  const [usersLoading, setUsersLoading] = useState(false);
  const [userSearchRole, setUserSearchRole] = useState("");
  const [revenueReport, setRevenueReport] = useState(null);
  const [reportLoading, setReportLoading] = useState(false);
  const [orderStatusById, setOrderStatusById] = useState({});
  const [stockByProductId, setStockByProductId] = useState({});

  const imageNames = [
    "Badminton",
    "Bed",
    "Boardgame",
    "Camera",
    "Chair",
    "Comicbook",
    "Face Cream",
    "CricketBat",
    "Dal",
    "Dictionary",
    "Doll",
    "Football",
    "Grater",
    "HeadPhones",
    "Jacket",
    "Jeans",
    "Keyboard",
    "Knife",
    "Kurta",
    "Laptop",
    "Lipstick",
    "Microwave",
    "Mixer",
    "Mouse",
    "Notebook",
    "Novel",
    "Oil",
    "Pen",
    "Perfume",
    "Puzzle",
    "Refrigerator",
    "Rice",
    "Sandals",
    "Saree",
    "Scissors",
    "Shampoo",
    "Shoes",
    "SmartPhone",
    "SmartWatch",
    "Sneakers",
    "Sofa",
    "Speaker",
    "Stapler",
    "Sugar",
    "Table",
    "Tablet",
    "Textbook",
    "Toycar",
    "tShirt",
    "TV",
    "WashingMachine",
    "Yogamat",
  ];

  const getAuthToken = (data) => (
    data?.token ||
    data?.jwtToken ||
    data?.accessToken ||
    data?.jwt ||
    localStorage.getItem("token") ||
    ""
  );

  const getUserRole = (data) => (
    data?.role ||
    data?.userRole ||
    data?.authorities?.[0]?.authority ||
    ""
  );

  const getProductId = (product) => product?.productId || product?.id;
  const getOrderId = (order) => order?.orderId || order?.id || order?.orderItemId;
  const getUserId = (user) => user?.userId || user?.id;

  const normalizeName = (value = "") => value.toLowerCase().replace(/[^a-z0-9]/g, "");

  const productImageSrc = (productName = "") => {
    const normalizedProduct = normalizeName(productName);
    const imageName = imageNames.find((name) => normalizeName(name) === normalizedProduct);
    return imageName ? `/images/${imageName}.jpg` : "/images/Inventory.jpg";
  };

  const authHeaders = () => ({
    Authorization: `Bearer ${localStorage.getItem("token") || ""}`,
  });

  const getProductList = (data) => {
    if (Array.isArray(data)) {
      return data;
    }

    if (Array.isArray(data?.data)) {
      return data.data;
    }

    if (Array.isArray(data?.products)) {
      return data.products;
    }

    if (Array.isArray(data?.productList)) {
      return data.productList;
    }

    if (Array.isArray(data?.content)) {
      return data.content;
    }

    return [];
  };

  const getOrderList = (data) => {
    if (Array.isArray(data)) {
      return data;
    }

    if (Array.isArray(data?.data)) {
      return data.data;
    }

    if (Array.isArray(data?.orderItems)) {
      return data.orderItems;
    }

    if (Array.isArray(data?.orders)) {
      return data.orders;
    }

    if (Array.isArray(data?.content)) {
      return data.content;
    }

    return [];
  };

  const getUserList = (data) => {
    if (Array.isArray(data)) {
      return data;
    }

    if (Array.isArray(data?.data)) {
      return data.data;
    }

    if (Array.isArray(data?.users)) {
      return data.users;
    }

    if (Array.isArray(data?.content)) {
      return data.content;
    }

    return [];
  };

  const fetchProducts = async (authToken) => {
    const token = authToken || localStorage.getItem("token");

    if (!token) {
      setProducts([]);
      setProductsError("Login token not found. Please login again.");
      return false;
    }

    try {
      setProductsLoading(true);
      setProductsError("");

      const response = await axios.get(`${BASE_URL}/products`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      setProducts(getProductList(response.data));
      return true;
    } catch (error) {
      const status = error.response?.status;
      const message = status === 403
        ? "Cannot fetch products: customer is not allowed to access /products, or the login token is invalid."
        : "Cannot fetch products. Please check backend product API.";

      setProducts([]);
      setProductsError(message);
      alert(message);
      return false;
    } finally {
      setProductsLoading(false);
    }
  };

  const login = async () => {
    try {
      const response = await axios.post(`${BASE_URL}/auth/login`, {
        emailId,
        password,
      });

      alert(response.data.message);

      const token = getAuthToken(response.data);
      const loggedInRole = getUserRole(response.data);

      localStorage.setItem("token", token);
      localStorage.setItem("role", loggedInRole);
      localStorage.setItem("userId", response.data.userId);
      localStorage.setItem("userName", response.data.userName);
      localStorage.setItem("emailId", response.data.emailId);
      localStorage.setItem("phoneNo", response.data.phoneNo || "");

      if (loggedInRole === "CUSTOMER") {
        await fetchProducts(token);
        setPage("customerDashboard");
      } else if (loggedInRole === "VENDOR") {
        await fetchProducts(token);
        setPage("vendorDashboard");
      } else if (loggedInRole === "MANAGER") {
        await fetchProducts(token);
        setPage("managerDashboard");
      } else if (loggedInRole === "ADMIN") {
        setPage("adminDashboard");
      }
    } catch (error) {
      alert("Invalid Email or Password");
      console.log(error);
    }
  };

  const register = async () => {
    try {
      await axios.post(`${BASE_URL}/auth/register`, {
        userName,
        emailId,
        password,
        phoneNo,
        role: role || "CUSTOMER",
      });

      alert("Registration Successful");
      setPage("login");
    } catch (error) {
      alert("Registration Failed");
      console.log(error);

console.log(error.response);

console.log(error.response.data);

alert(

JSON.stringify(

error.response.data,

null,

2

)

);
    }
  };

  const getProducts = async () => {
    await fetchProducts();
  };

  const logout = () => {
    localStorage.clear();
    setProducts([]);
    setOrderItems([]);
    setSelectedProduct(null);
    setPlacedOrder(null);
    setProductsLoading(false);
    setProductsError("");
    setStockByProductId({});
    setPage("login");
  };

  const resetProductForm = () => {
    setEditingProduct(null);
    setProductForm({
      productName: "",
      description: "",
      price: "",
      categoryType: "",
      stockQuantity: "",
    });
  };

  const openAdminProducts = async () => {
    setPage("adminProducts");
    await fetchProducts();
  };

  const openAddProductForm = () => {
    resetProductForm();
    setShowProductForm(true);
  };

  const openAddProduct = () => {
    setShowAddressForm(true);
  };

  const openEditProduct = (product) => {
    setEditingProduct(product);
    setProductForm({
      productName: product.productName || "",
      description: product.description || "",
      price: product.price || "",
      categoryType: product.categoryType || "",
      stockQuantity: product.stockQuantity || "",
    });
    setShowProductForm(true);
  };

  const saveProduct = async () => {
    const payload = {
      ...productForm,
      price: Number(productForm.price),
      stockQuantity: Number(productForm.stockQuantity),
    };

    try {
      if (editingProduct) {
        const productId = getProductId(editingProduct);
        await axios.put(`${BASE_URL}/products/${productId}`, payload, { headers: authHeaders() });
        alert("Product updated successfully");
      } else {
        await axios.post(`${BASE_URL}/products/add`, payload, { headers: authHeaders() });
        alert("Product added successfully");
      }

      setShowProductForm(false);
      resetProductForm();
      await fetchProducts();
    } catch (error) {
      alert("Product save failed. Please check backend product API.");
      console.log(error);
    }
  };

  const deleteProduct = async (product) => {
    const productId = getProductId(product);

    if (!window.confirm(`Delete ${product.productName}?`)) {
      return;
    }

    try {
      await axios.delete(`${BASE_URL}/products/${productId}`, { headers: authHeaders() });
      alert("Product deleted successfully");
      await fetchProducts();
    } catch (error) {
      alert("Delete failed. Please check backend product API.");
      console.log(error);
    }
  };

  const updateProductStock = async (product) => {
    const productId = getProductId(product);
    const nextStock = stockByProductId[productId] ?? product.stockQuantity;

    if (nextStock === "" || nextStock === null || nextStock === undefined) {
      alert("Please enter stock quantity.");
      return;
    }

    const payload = {
      productName: product.productName || "",
      description: product.description || "",
      price: Number(product.price || 0),
      categoryType: product.categoryType || "",
      stockQuantity: Number(nextStock),
    };

    try {
      await axios.put(`${BASE_URL}/products/${productId}`, payload, { headers: authHeaders() });
      alert("Stock updated successfully");
      await fetchProducts();
    } catch (error) {
      alert("Stock update failed. Please check backend product API.");
      console.log(error);
    }
  };

  const fetchOrderItems = async (displayPanel = true, onlyCurrentUser = displayPanel) => {
    try {
      setOrdersLoading(true);
      setShowOrders(displayPanel);

      const userId = localStorage.getItem("userId");
      const response = await axios.get(`${BASE_URL}/orderItems`, {
        params: onlyCurrentUser && userId ? { userId } : {},
        headers: authHeaders(),
      });

      setOrderItems(getOrderList(response.data));
    } catch (error) {
      alert("Cannot fetch order items. Please check backend orderItems API.");
      console.log(error);
    } finally {
      setOrdersLoading(false);
    }
  };

  const openAdminOrderReport = async () => {
    setPage("adminOrderReport");
    await fetchOrderItems(false);
  };

  const openManageOrders = async () => {
    setPage("adminManageOrders");
    await fetchOrderItems(false);
  };

  const openManagerProducts = async () => {
    setPage("managerProducts");
    await fetchProducts();
  };

  const openManagerOrderReport = async () => {
    setPage("managerOrderReport");
    await fetchOrderItems(false);
  };

  const fetchUsers = async () => {
    try {
      setUsersLoading(true);
      const response = await axios.get(`${BASE_URL}/users`, { headers: authHeaders() });
       console.log(JSON.stringify(response.data[0], null, 2));

    setUsers(response.data);

    } catch (error) {
       console.log("Status:", error.response?.status);
    console.log("Error message:", error.response?.data?.message);
    console.log("Full error data:", JSON.stringify(error.response?.data, null, 2));
    alert("Cannot fetch users. Please check backend users API.");
    } finally {
      setUsersLoading(false);
    }
  };

  const openViewUsers = async () => {
    setPage("adminUsers");
    await fetchUsers();
  };

  const fetchRevenueReport = async () => {
    try {
      setReportLoading(true);
      const response = await axios.get(`${BASE_URL}/dashboard/revenue`, { headers: authHeaders() });
      setRevenueReport(response.data);
    } catch (error) {
      alert("Cannot fetch revenue report. Please check backend report API.");
      console.log(error);
    } finally {
      setReportLoading(false);
    }
  };

  const openRevenueReport = async () => {
    setPage("adminRevenueReport");
    await fetchRevenueReport();
  };

  const updateOrderStatus = async (order) => {
    const orderId = getOrderId(order);
    const status = orderStatusById[orderId] || order.status || order.orderStatus || "PLACED";

    try {
      axios.put(
`${BASE_URL}/orders/status/${orderId}`,
{
  orderStatus: status
},
{
  headers: authHeaders()
}
);
      alert("Order status updated successfully");
      await fetchOrderItems(false);
    } catch (error) {
      alert("Order status update failed. Please check backend order status API.");
      console.log(error);
    }
  };

  const cancelOrder = async (order) => {
    const orderId = getOrderId(order);

    if (!window.confirm("Cancel this order?")) {
      return;
    }

    try {
      await axios.delete(`${BASE_URL}/orderItems/${orderId}`, { headers: authHeaders() });
      alert("Order cancelled successfully");
      await fetchOrderItems();
    } catch (error) {
      alert("Cancel order failed. Please check backend order API.");
      console.log(error);
    }
  };

  const buyNow = (product) => {
    setSelectedProduct(product);
    setOrderQuantity(1);
    setPlacedOrder(null);
    setPage("placeOrder");
  };

  const getAddressText = () => {
    const parts = [
      address.houseNo,
      address.street,
      address.city,
      address.state,
      address.pinCode,
      address.country,
    ].filter(Boolean);

    return parts.join(", ");
  };

  const hasAddress = () => Boolean(
    address.houseNo &&
    address.street &&
    address.city &&
    address.state &&
    address.pinCode &&
    address.country
  );

  const saveAddress = async () => {
    if (!hasAddress()) {
      alert("Please fill all address fields.");
      return;
    }

    const payload = {
      userId: localStorage.getItem("userId"),
      houseNo: address.houseNo,
      street: address.street,
      city: address.city,
      distt:address.distt,
      state: address.state,
      pinCode: Number(address.pinCode),
    };
    console.log(payload);

    try {
      
      const response = await axios.post(`${BASE_URL}/userAddress/add`, payload, { headers: authHeaders() });
      const saved = response.data?.data || response.data || {};
      const newAddress = { ...payload, ...saved };

      setAddressId(saved.addressId || saved.id || addressId);
      localStorage.setItem("customerAddress", JSON.stringify(newAddress));
      setShowAddressForm(false);
      alert("Address saved successfully");
    } catch (error) {
      alert("Address save failed. Please check backend userAddress/add API.");
      console.log(error);
      console.log(error.response);

console.log(error.response.data);

alert(

JSON.stringify(

error.response.data,

null,

2

)

);
    }
  };

  const placeOrder = async () => {
    if (!selectedProduct) {
      return;
    }

    if (!hasAddress()) {
      alert("Please add your delivery address before placing the order.");
      setShowAddressForm(true);
      return;
    }

    const payload = {
      userId: localStorage.getItem("userId"),
      productId: getProductId(selectedProduct),
      quantity: Number(orderQuantity),
      totalAmount: Number(selectedProduct.price || 0) * Number(orderQuantity),
      addressId,
      address: getAddressText(),
    };

    try {
      console.log(payload);
      const response = await axios.post(`${BASE_URL}/orders/placeOrder`, payload, { headers: authHeaders() });
      setPlacedOrder(response.data);
      setPage("orderPayment");
    } catch (error) {
      alert("Place order failed. Please check backend placeOrder API.");
      console.log(error);
      console.log(error.response);

console.log(error.response.data);

alert(

JSON.stringify(

error.response.data,

null,

2

)

);
    }
  };

  const orderPayment = async () => {
    const orderId = placedOrder?.orderId || placedOrder?.id || placedOrder?.data?.orderId;
   const payload = {

  order_id: orderId,

  paymentMethod: paymentMode,

  paymentStatus: "SUCCESS",

  transaction_id:
  "TXN" + Date.now(),

  amountPaid:

  Number(selectedProduct?.price || 0)

  * Number(orderQuantity),
  paymentDate:

new Date().toISOString()

};
    console.log("Placed Order", placedOrder);

console.log("Order ID", orderId);
    console.log(payload);

    try {
      await axios.post(`${BASE_URL}/orderPayment/add`, payload, { headers: authHeaders() });
      alert("Order placed successfully");
      await fetchProducts();
      setPage("customerDashboard");
    } catch(error){

console.log(error.response);

console.log(error.response.data);

alert(

JSON.stringify(

error.response.data,

null,

2

)

);

}
  };

  const categories = useMemo(() => {
    const values = products
      .map((product) => product.categoryType)
      .filter(Boolean);

    return ["ALL", ...Array.from(new Set(values))];
  }, [products]);

  const filteredProducts = useMemo(() => {
    const normalizedSearch = searchText.trim().toLowerCase();

    return products.filter((product) => {
      const matchesSearch = (product.productName || "").toLowerCase().includes(normalizedSearch);
      const matchesCategory = selectedCategory === "ALL" || product.categoryType === selectedCategory;
      return matchesSearch && matchesCategory;
    });
  }, [products, searchText, selectedCategory]);

  const filteredUsers = useMemo(() => {
    const normalizedRole = userSearchRole.trim().toLowerCase();

    if (!normalizedRole) {
      return users;
    }

    return users.filter((user) => (
      String(user.role || user.userRole || "").toLowerCase().includes(normalizedRole) ||
      String(getUserId(user) || "").toLowerCase().includes(normalizedRole)
    ));
  }, [users, userSearchRole]);

  const formInputStyle = {
    width: "100%",
    padding: "12px",
    marginTop: "15px",
    border: "1px solid #ddd",
    borderRadius: "10px",
    fontSize: "16px",
    boxSizing: "border-box",
  };

  const primaryButtonStyle = {
    width: "100%",
    padding: "12px",
    marginTop: "20px",
    background: "#4f46e5",
    color: "white",
    border: "none",
    borderRadius: "10px",
    fontSize: "16px",
    cursor: "pointer",
  };

  const smallButtonStyle = {
    padding: "10px 14px",
    border: "none",
    borderRadius: "8px",
    cursor: "pointer",
    fontWeight: "bold",
  };

  const adminPageStyle = {
    width: "94%",
    maxWidth: "1280px",
    margin: "24px auto",
    padding: "24px",
    background: "rgba(255,255,255,0.96)",
    borderRadius: "8px",
    boxShadow: "0 10px 30px rgba(0,0,0,0.2)",
  };

  const adminDashboardShellStyle = {
    display: "flex",
    justifyContent: "flex-end",
    alignItems: "center",
    minHeight: "100vh",
    width: "100%",
    paddingRight: "70px",
    boxSizing: "border-box",
  };

  const adminDashboardCardStyle = {
    width: "440px",
    minHeight: "460px",
    padding: "30px",
    background: "rgb(255, 249, 249)",
    backdropFilter: "blur(15px)",
    border: "1px solid rgba(255,255,255,0.3)",
    borderRadius: "20px",
    boxShadow: "0 10px 30px rgba(0,0,0,0.3)",
    textAlign: "center",
    display: "flex",
    flexDirection: "column",
  };

  const adminButtonStyle = {
    ...smallButtonStyle,
    minWidth: "210px",
    padding: "14px 18px",
    background: "#2563eb",
    color: "white",
  };

  const adminDashboardButtonStyle = {
    ...adminButtonStyle,
    width: "100%",
    minWidth: 0,
    minHeight: "48px",
  };

  const renderProductForm = () => showProductForm && (
    <div
      style={{
        marginBottom: "20px",
        padding: "18px",
        background: "#f8fafc",
        border: "1px solid #e2e8f0",
        borderRadius: "8px",
      }}
    >
      <h3 style={{ marginTop: 0 }}>{editingProduct ? "Edit Product" : "Add Product"}</h3>
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))", gap: "12px" }}>
        <input
          placeholder="Product Name"
          value={productForm.productName}
          onChange={(e) => setProductForm({ ...productForm, productName: e.target.value })}
          style={formInputStyle}
        />
        <input
          placeholder="Description"
          value={productForm.description}
          onChange={(e) => setProductForm({ ...productForm, description: e.target.value })}
          style={formInputStyle}
        />
        <input
          type="number"
          placeholder="Price"
          value={productForm.price}
          onChange={(e) => setProductForm({ ...productForm, price: e.target.value })}
          style={formInputStyle}
        />
        <input
          placeholder="Category"
          value={productForm.categoryType}
          onChange={(e) => setProductForm({ ...productForm, categoryType: e.target.value })}
          style={formInputStyle}
        />
        <input
          type="number"
          placeholder="Stock"
          value={productForm.stockQuantity}
          onChange={(e) => setProductForm({ ...productForm, stockQuantity: e.target.value })}
          style={formInputStyle}
        />
      </div>
      <div style={{ display: "flex", gap: "10px", marginTop: "16px" }}>
        <button onClick={saveProduct} style={{ ...smallButtonStyle, background: "#16a34a", color: "white" }}>
          Save
        </button>
        <button
          onClick={() => {
            setShowProductForm(false);
            resetProductForm();
          }}
          style={{ ...smallButtonStyle, background: "#e5e7eb" }}
        >
          Cancel
        </button>
      </div>
    </div>
  );

  const renderAddressForm = () => showAddressForm && (
    <div
      style={{
        marginBottom: "20px",
        padding: "18px",
        background: "#f8fafc",
        border: "1px solid #e2e8f0",
        borderRadius: "8px",
      }}
    >
      <h3 style={{ marginTop: 0 }}>{hasAddress() ? "Change Address" : "Add Address"}</h3>
      <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(180px, 1fr))", gap: "12px" }}>
        <input
          placeholder="House No"
          value={address.houseNo}
          onChange={(e) => setAddress({ ...address, houseNo: e.target.value })}
          style={{ ...formInputStyle, marginTop: 0 }}
        />
        <input
          placeholder="Street"
          value={address.street}
          onChange={(e) => setAddress({ ...address, street: e.target.value })}
          style={{ ...formInputStyle, marginTop: 0 }}
        />
        <input
          placeholder="City"
          value={address.city}
          onChange={(e) => setAddress({ ...address, city: e.target.value })}
          style={{ ...formInputStyle, marginTop: 0 }}
        />
        <input
          placeholder="State"
          value={address.state}
          onChange={(e) => setAddress({ ...address, state: e.target.value })}
          style={{ ...formInputStyle, marginTop: 0 }}
        />
        <input
          placeholder="Pin Code"
          value={address.pinCode}
          onChange={(e) => setAddress({ ...address, pinCode: e.target.value })}
          style={{ ...formInputStyle, marginTop: 0 }}
        />
        <input
          placeholder="Country"
          value={address.country}
          onChange={(e) => setAddress({ ...address, country: e.target.value })}
          style={{ ...formInputStyle, marginTop: 0 }}
        />
      </div>
      <div style={{ display: "flex", gap: "10px", marginTop: "16px" }}>
        <button onClick={saveAddress} style={{ ...smallButtonStyle, background: "#16a34a", color: "white" }}>
          Save Address
        </button>
        <button onClick={() => setShowAddressForm(false)} style={{ ...smallButtonStyle, background: "#e5e7eb" }}>
          Cancel
        </button>
      </div>
    </div>
  );

  const renderOrdersPanel = () => showOrders && (
    <aside
      style={{
        width: "310px",
        background: "#ffffff",
        border: "1px solid #e5e7eb",
        borderRadius: "8px",
        padding: "16px",
        boxShadow: "0 8px 18px rgba(15,23,42,0.12)",
      }}
    >
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
        <h3 style={{ margin: 0 }}>My Orders</h3>
        <button onClick={() => setShowOrders(false)} style={{ ...smallButtonStyle, padding: "7px 10px" }}>
          Close
        </button>
      </div>
      {ordersLoading ? (
        <p>Loading orders...</p>
      ) : orderItems.length === 0 ? (
        <p>No order items found.</p>
      ) : (
        <div style={{ display: "grid", gap: "12px", marginTop: "14px" }}>
          {orderItems.map((order) => (
            <div key={getOrderId(order)} style={{ border: "1px solid #e5e7eb", borderRadius: "8px", padding: "12px" }}>
              <strong>{order.productName || order.product?.productName || `Order #${getOrderId(order)}`}</strong>
              <p style={{ margin: "6px 0" }}>Quantity: {order.quantity || order.orderQuantity || 1}</p>
              <p style={{ margin: "6px 0" }}>Status: {order.status || order.orderStatus || "Placed"}</p>
              <button
                onClick={() => cancelOrder(order)}
                style={{ ...smallButtonStyle, background: "#dc2626", color: "white", width: "100%" }}
              >
                Cancel Order
              </button>
            </div>
          ))}
        </div>
      )}
    </aside>
  );

  const renderAdminHeader = (title) => (
    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "12px", flexWrap: "wrap", marginBottom: "20px" }}>
      <h1 style={{ margin: 0, fontSize: "30px", fontWeight: "bold" }}>{title}</h1>
      {title !== "ADMIN DASHBOARD" && (
        <button onClick={() => setPage("adminDashboard")} style={{ ...smallButtonStyle, background: "#e5e7eb" }}>
          Back
        </button>
      )}
    </div>
  );

  const renderAdminDashboard = () => (
    <div style={adminDashboardShellStyle}>
      <div style={adminDashboardCardStyle}>
        <h1 style={{ margin: "0 0 24px", fontSize: "30px", fontWeight: "bold" }}>ADMIN DASHBOARD</h1>
        <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "24px", flex: 1, alignContent: "space-evenly" }}>
          <button onClick={openAdminProducts} style={adminDashboardButtonStyle}>View Products</button>
          <button onClick={openAdminOrderReport} style={adminDashboardButtonStyle}>Order Report</button>
          <button onClick={openViewUsers} style={adminDashboardButtonStyle}>View Users</button>
          <button onClick={openRevenueReport} style={adminDashboardButtonStyle}>Revenue</button>
          <button onClick={openManageOrders} style={adminDashboardButtonStyle}>Manage Orders</button>
          <button onClick={logout} style={adminDashboardButtonStyle}>LogOut</button>
        </div>
      </div>
    </div>
  );

  const renderVendorDashboard = () => (
    <div style={adminPageStyle}>
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "12px", flexWrap: "wrap", marginBottom: "20px" }}>
        <h1 style={{ margin: 0, fontSize: "30px", fontWeight: "bold" }}>VENDOR DASHBOARD</h1>
        <button onClick={logout} style={{ ...smallButtonStyle, background: "#111827", color: "white" }}>
          LogOut
        </button>
      </div>

      <div style={{ display: "flex", gap: "10px", flexWrap: "wrap", marginBottom: "18px" }}>
        <button onClick={openAddProductForm} style={{ ...smallButtonStyle, background: "#16a34a", color: "white" }}>
          Add Product
        </button>
        <button onClick={() => fetchProducts()} style={{ ...smallButtonStyle, background: "#2563eb", color: "white" }}>
          Refresh Products
        </button>
      </div>

      {renderProductForm()}

      <h2 style={{ marginTop: 0 }}>Product List</h2>
      {productsLoading ? (
        <p>Loading products...</p>
      ) : productsError ? (
        <p style={{ color: "#b91c1c", fontWeight: "bold" }}>{productsError}</p>
      ) : products.length === 0 ? (
        <p>No products found.</p>
      ) : (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(240px, 1fr))", gap: "18px" }}>
          {products.map((product) => {
            const productId = getProductId(product);

            return (
              <article key={productId} style={{ border: "1px solid #e5e7eb", borderRadius: "8px", overflow: "hidden", background: "#fff" }}>
                <img src={productImageSrc(product.productName)} alt={product.productName} style={{ width: "100%", height: "150px", objectFit: "cover", background: "#f3f4f6" }} />
                <div style={{ padding: "14px", display: "grid", gap: "8px" }}>
                  <h3 style={{ margin: 0 }}>{product.productName}</h3>
                  <span>Price: Rs. {product.price}</span>
                  <span>Category: {product.categoryType}</span>
                  <span>Current Stock: {product.stockQuantity}</span>
                  <input
                    type="number"
                    min="0"
                    placeholder="Update stock"
                    value={stockByProductId[productId] ?? product.stockQuantity ?? ""}
                    onChange={(e) => setStockByProductId({ ...stockByProductId, [productId]: e.target.value })}
                    style={{ ...formInputStyle, marginTop: 0 }}
                  />
                  <button onClick={() => updateProductStock(product)} style={{ ...smallButtonStyle, background: "#16a34a", color: "white" }}>
                    Update Stock
                  </button>
                </div>
              </article>
            );
          })}
        </div>
      )}
    </div>
  );

  const renderManagerHeader = (title) => (
    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center", gap: "12px", flexWrap: "wrap", marginBottom: "20px" }}>
      <h1 style={{ margin: 0, fontSize: "30px", fontWeight: "bold" }}>{title}</h1>
      {title !== "MANAGER DASHBOARD" && (
        <button onClick={() => setPage("managerDashboard")} style={{ ...smallButtonStyle, background: "#e5e7eb" }}>
          Back
        </button>
      )}
    </div>
  );

  const renderManagerDashboard = () => (
    <div style={adminPageStyle}>
      {renderManagerHeader("MANAGER DASHBOARD")}
      <div style={{ display: "flex", justifyContent: "space-between", gap: "12px", flexWrap: "wrap", marginBottom: "18px" }}>
        <div style={{ display: "flex", gap: "10px", flexWrap: "wrap" }}>
          <button onClick={openAddProductForm} style={{ ...smallButtonStyle, background: "#16a34a", color: "white" }}>
            Add Product
          </button>
          <button onClick={openManagerOrderReport} style={{ ...smallButtonStyle, background: "#2563eb", color: "white" }}>
            Order Report
          </button>
          <button onClick={() => fetchProducts()} style={{ ...smallButtonStyle, background: "#2563eb", color: "white" }}>
            Refresh
          </button>
        </div>
        <button onClick={logout} style={{ ...smallButtonStyle, background: "#111827", color: "white" }}>
          LogOut
        </button>
      </div>

      {renderProductForm()}

      <h2 style={{ marginTop: 0 }}>Product List</h2>
      {productsLoading ? (
        <p>Loading products...</p>
      ) : productsError ? (
        <p style={{ color: "#b91c1c", fontWeight: "bold" }}>{productsError}</p>
      ) : products.length === 0 ? (
        <p>No products found.</p>
      ) : (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(240px, 1fr))", gap: "18px" }}>
          {products.map((product) => {
            const productId = getProductId(product);

            return (
              <article key={productId} style={{ border: "1px solid #e5e7eb", borderRadius: "8px", overflow: "hidden", background: "#fff" }}>
                <img src={productImageSrc(product.productName)} alt={product.productName} style={{ width: "100%", height: "150px", objectFit: "cover", background: "#f3f4f6" }} />
                <div style={{ padding: "14px", display: "grid", gap: "8px" }}>
                  <h3 style={{ margin: 0 }}>{product.productName}</h3>
                  <span>Price: Rs. {product.price}</span>
                  <span>Category: {product.categoryType}</span>
                  <span>Current Stock: {product.stockQuantity}</span>
                  <input
                    type="number"
                    min="0"
                    placeholder="Update stock"
                    value={stockByProductId[productId] ?? product.stockQuantity ?? ""}
                    onChange={(e) => setStockByProductId({ ...stockByProductId, [productId]: e.target.value })}
                    style={{ ...formInputStyle, marginTop: 0 }}
                  />
                  <button onClick={() => updateProductStock(product)} style={{ ...smallButtonStyle, background: "#16a34a", color: "white" }}>
                    Update Stock
                  </button>
                  <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "8px" }}>
                    <button onClick={() => openEditProduct(product)} style={{ ...smallButtonStyle, background: "#dbeafe" }}>Edit</button>
                    <button onClick={() => deleteProduct(product)} style={{ ...smallButtonStyle, background: "#fee2e2" }}>Delete</button>
                  </div>
                </div>
              </article>
            );
          })}
        </div>
      )}
    </div>
  );

  const renderManagerProducts = () => (
    <div style={adminPageStyle}>
      {renderManagerHeader("Product Page")}
      <div style={{ display: "flex", gap: "10px", flexWrap: "wrap", marginBottom: "18px" }}>
        <button onClick={openAddProductForm} style={{ ...smallButtonStyle, background: "#16a34a", color: "white" }}>
          Add Product
        </button>
        <button onClick={() => fetchProducts()} style={{ ...smallButtonStyle, background: "#2563eb", color: "white" }}>
          Refresh
        </button>
      </div>
      {renderProductForm()}
      {productsLoading ? (
        <p>Loading products...</p>
      ) : productsError ? (
        <p style={{ color: "#b91c1c", fontWeight: "bold" }}>{productsError}</p>
      ) : products.length === 0 ? (
        <p>No products found.</p>
      ) : (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(230px, 1fr))", gap: "18px" }}>
          {products.map((product) => (
            <article key={getProductId(product)} style={{ border: "1px solid #e5e7eb", borderRadius: "8px", overflow: "hidden", background: "#fff" }}>
              <img src={productImageSrc(product.productName)} alt={product.productName} style={{ width: "100%", height: "150px", objectFit: "cover", background: "#f3f4f6" }} />
              <div style={{ padding: "14px", display: "grid", gap: "8px" }}>
                <h3 style={{ margin: 0 }}>{product.productName}</h3>
                <span>Price: Rs. {product.price}</span>
                <span>Category: {product.categoryType}</span>
                <span>Stock: {product.stockQuantity}</span>
                <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "8px" }}>
                  <button onClick={() => openEditProduct(product)} style={{ ...smallButtonStyle, background: "#dbeafe" }}>Edit</button>
                  <button onClick={() => deleteProduct(product)} style={{ ...smallButtonStyle, background: "#fee2e2" }}>Delete</button>
                </div>
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
  );

  const renderManagerOrderReport = () => (
    <div style={adminPageStyle}>
      {renderManagerHeader("Order Report")}
      <button onClick={() => fetchOrderItems(false)} style={{ ...smallButtonStyle, background: "#2563eb", color: "white", marginBottom: "16px" }}>
        Refresh Orders
      </button>
      {ordersLoading ? (
        <p>Loading orders...</p>
      ) : orderItems.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(2, minmax(0, 1fr))", gap: "16px" }}>
          {orderItems.map((order) => {
            const orderId = getOrderId(order);
            const productName = order.productName || order.product?.productName || "N/A";
            const currentStatus = orderStatusById[orderId] || order.status || order.orderStatus || "PLACED";

            return (
              <div key={orderId} style={{ border: "1px solid #e5e7eb", borderRadius: "8px", padding: "12px", background: "#fff", display: "grid", gridTemplateColumns: "90px 1fr", gap: "14px", alignItems: "start" }}>
                <img src={productImageSrc(productName)} alt={productName} style={{ width: "90px", height: "90px", objectFit: "cover", borderRadius: "8px", background: "#f3f4f6" }} />
                <div>
                  <strong>Order #{orderId}</strong>
                  <p style={{ margin: "6px 0" }}>Product: {productName}</p>
                  <p style={{ margin: "6px 0" }}>User ID: {order.userId || order.user?.userId || order.customerId || "N/A"}</p>
                  <p style={{ margin: "6px 0" }}>Quantity: {order.quantity || order.orderQuantity || 1}</p>
                  <p style={{ margin: "6px 0" }}>Amount: Rs. {order.totalAmount || order.amount || order.price || 0}</p>
                  <div style={{ display: "flex", gap: "10px", flexWrap: "wrap", alignItems: "center", marginTop: "10px" }}>
                    <select
                      value={currentStatus}
                      onChange={(e) => setOrderStatusById({ ...orderStatusById, [orderId]: e.target.value })}
                      style={{ ...formInputStyle, maxWidth: "220px", marginTop: 0 }}
                    >
                      <option>PLACED</option>
                      <option>CONFIRMED</option>
                      <option>SHIPPED</option>
                      <option>DELIVERED</option>
                      <option>CANCELLED</option>
                    </select>
                    <button onClick={() => updateOrderStatus(order)} style={{ ...smallButtonStyle, background: "#16a34a", color: "white" }}>
                      Update Status
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );

  const renderAdminProducts = () => (
    <div style={adminPageStyle}>
      {renderAdminHeader("View Products")}
      <div style={{ display: "flex", gap: "10px", flexWrap: "wrap", marginBottom: "18px" }}>
        <button onClick={openAddProductForm} style={{ ...smallButtonStyle, background: "#16a34a", color: "white" }}>
          Add Product
        </button>
        <button onClick={() => fetchProducts()} style={{ ...smallButtonStyle, background: "#2563eb", color: "white" }}>
          Refresh
        </button>
      </div>
      {renderProductForm()}
      {productsLoading ? (
        <p>Loading products...</p>
      ) : productsError ? (
        <p style={{ color: "#b91c1c", fontWeight: "bold" }}>{productsError}</p>
      ) : (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(230px, 1fr))", gap: "18px" }}>
          {products.map((product) => (
            <article key={getProductId(product)} style={{ border: "1px solid #e5e7eb", borderRadius: "8px", overflow: "hidden", background: "#fff" }}>
              <img src={productImageSrc(product.productName)} alt={product.productName} style={{ width: "100%", height: "150px", objectFit: "cover", background: "#f3f4f6" }} />
              <div style={{ padding: "14px", display: "grid", gap: "8px" }}>
                <h3 style={{ margin: 0 }}>{product.productName}</h3>
                <span>Price: Rs. {product.price}</span>
                <span>Category: {product.categoryType}</span>
                <span>Stock: {product.stockQuantity}</span>
                <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: "8px" }}>
                  <button onClick={() => openEditProduct(product)} style={{ ...smallButtonStyle, background: "#dbeafe" }}>Edit</button>
                  <button onClick={() => deleteProduct(product)} style={{ ...smallButtonStyle, background: "#fee2e2" }}>Delete</button>
                </div>
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
  );

  const renderAdminOrderReport = () => (
    <div style={adminPageStyle}>
      {renderAdminHeader("Order Report")}
      <button onClick={() => fetchOrderItems(false)} style={{ ...smallButtonStyle, background: "#2563eb", color: "white", marginBottom: "16px" }}>
        Refresh Orders
      </button>
      {ordersLoading ? (
        <p>Loading orders...</p>
      ) : orderItems.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        <div style={{ display: "grid", gap: "12px" }}>
          {orderItems.map((order) => (
            <div key={getOrderId(order)} style={{ border: "1px solid #e5e7eb", borderRadius: "8px", padding: "12px", background: "#fff" }}>
              <strong>Order #{getOrderId(order)}</strong>
              <p style={{ margin: "6px 0" }}>Product: {order.productName || order.product?.productName || getProductId(order.product) || "N/A"}</p>
              <p style={{ margin: "6px 0" }}>User ID: {order.userId || order.user?.userId || order.customerId || "N/A"}</p>
              <p style={{ margin: "6px 0" }}>Quantity: {order.quantity || order.orderQuantity || 1}</p>
              <p style={{ margin: "6px 0" }}>Status: {order.status || order.orderStatus || "Placed"}</p>
              <p style={{ margin: "6px 0" }}>Amount: Rs. {order.totalAmount || order.amount || order.price || 0}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );

  const renderAdminUsers = () =>  {

  console.log(filteredUsers);

  return (
    <div style={adminPageStyle}>
      {renderAdminHeader("View Users")}
      <div style={{ display: "flex", gap: "10px", flexWrap: "wrap", marginBottom: "16px" }}>
        <input
          placeholder="Search by role or ID"
          value={userSearchRole}
          onChange={(e) => setUserSearchRole(e.target.value)}
          style={{ ...formInputStyle, maxWidth: "280px", marginTop: 0 }}
        />
        <button onClick={fetchUsers} style={{ ...smallButtonStyle, background: "#2563eb", color: "white" }}>Refresh Users</button>
      </div>
      {usersLoading ? (
        <p>Loading users...</p>
      ) : filteredUsers.length === 0 ? (
        <p>No users found.</p>
      ) : (
        <div style={{ display: "grid", gap: "12px" }}>
          {filteredUsers.map((user,index) => {

  console.log(
    "User:",
    user.userName,
    "ID:",
    getUserId(user)
  );

  return (

            <div key={`${getUserId(user)}-${index}`} style={{ border: "1px solid #e5e7eb", borderRadius: "8px", padding: "12px", background: "#fff" }}>
              <strong>{user.userName || user.name || `User #${getUserId(user)}`}</strong>
              <p style={{ margin: "6px 0" }}>ID: {getUserId(user)}</p>
              <p style={{ margin: "6px 0" }}>Email: {user.emailId || user.email || "N/A"}</p>
              <p style={{ margin: "6px 0" }}>Phone: {user.phoneNo || user.phone || "N/A"}</p>
              <p style={{ margin: "6px 0" }}>Role: {user.role || user.userRole || "N/A"}</p>
            </div>
          );})}
        </div>
      )}
    </div>
 );};

  const renderAdminRevenueReport = () => (
    <div style={adminPageStyle}>
      {renderAdminHeader("Revenue")}
      <button onClick={fetchRevenueReport} style={{ ...smallButtonStyle, background: "#2563eb", color: "white", marginBottom: "16px" }}>
        Refresh Report
      </button>
      {reportLoading ? (
        <p>Loading report...</p>
      ) : revenueReport ? (
        <pre style={{ whiteSpace: "pre-wrap", background: "#111827", color: "white", padding: "16px", borderRadius: "8px", overflow: "auto" }}>
          {JSON.stringify(revenueReport, null, 2)}
        </pre>
      ) : (
        <p>No revenue report found.</p>
      )}
    </div>
  );

  const renderAdminManageOrders = () => (
    <div style={adminPageStyle}>
      {renderAdminHeader("Manage Orders")}
      <button onClick={() => fetchOrderItems(false)} style={{ ...smallButtonStyle, background: "#2563eb", color: "white", marginBottom: "16px" }}>
        Refresh Orders
      </button>
      {ordersLoading ? (
        <p>Loading orders...</p>
      ) : orderItems.length === 0 ? (
        <p>No orders found.</p>
      ) : (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(2, minmax(0, 1fr))", gap: "16px" }}>
          {orderItems.map((order) => {
            const orderId = getOrderId(order);
            const productName = order.productName || order.product?.productName || "N/A";
            const currentStatus = orderStatusById[orderId] || order.status || order.orderStatus || "PLACED";

            return (
              <div key={orderId} style={{ border: "1px solid #e5e7eb", borderRadius: "8px", padding: "12px", background: "#fff", display: "grid", gridTemplateColumns: "90px 1fr", gap: "14px", alignItems: "start" }}>
                <img src={productImageSrc(productName)} alt={productName} style={{ width: "90px", height: "90px", objectFit: "cover", borderRadius: "8px", background: "#f3f4f6" }} />
                <div>
                  <strong>Order #{orderId}</strong>
                  <p style={{ margin: "6px 0" }}>Product: {productName}</p>
                  <p style={{ margin: "6px 0" }}>User ID: {order.userId || order.user?.userId || order.customerId || "N/A"}</p>
                  <p style={{ margin: "6px 0" }}>Quantity: {order.quantity || order.orderQuantity || 1}</p>
                  <p style={{ margin: "6px 0" }}>Amount: Rs. {order.totalAmount || order.amount || order.price || 0}</p>
                  <div style={{ display: "flex", gap: "10px", flexWrap: "wrap", alignItems: "center", marginTop: "10px" }}>
                    <select
                      value={currentStatus}
                      onChange={(e) => setOrderStatusById({ ...orderStatusById, [orderId]: e.target.value })}
                      style={{ ...formInputStyle, maxWidth: "220px", marginTop: 0 }}
                    >
                      <option>PLACED</option>
                      <option>CONFIRMED</option>
                      <option>SHIPPED</option>
                      <option>DELIVERED</option>
                      <option>CANCELLED</option>
                    </select>
                    <button onClick={() => updateOrderStatus(order)} style={{ ...smallButtonStyle, background: "#16a34a", color: "white" }}>
                      Update Status
                    </button>
                  </div>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );

  const renderProducts = () => (
    <div
      style={{
        width: "94%",
        maxWidth: "1280px",
        margin: "24px auto",
        padding: "24px",
        background: "rgba(255,255,255,0.96)",
        borderRadius: "8px",
        boxShadow: "0 10px 30px rgba(0,0,0,0.2)",
      }}
    >
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          gap: "12px",
          flexWrap: "wrap",
          marginBottom: "18px",
        }}
      >
        <h1 style={{ margin: 0, fontSize: "30px", fontWeight: "bold" }}>CUSTOMER DASHBOARD</h1>

        <div style={{ display: "flex", gap: "10px", flexWrap: "wrap" }}>
          <button onClick={getProducts} style={{ ...smallButtonStyle, background: "#2563eb", color: "white" }}>
            Refresh Products
          </button>
          <button onClick={openAddProduct} style={{ ...smallButtonStyle, background: "#16a34a", color: "white" }}>
            {hasAddress() ? "Change Address" : "Add Address"}
          </button>
          <button onClick={logout} style={{ ...smallButtonStyle, background: "#111827", color: "white" }}>
            Logout
          </button>
        </div>
      </div>

      <div style={{ display: "flex", gap: "12px", flexWrap: "wrap", marginBottom: "18px" }}>
        <input
          placeholder="Search product by name"
          value={searchText}
          onChange={(e) => setSearchText(e.target.value)}
          style={{ ...formInputStyle, maxWidth: "320px", marginTop: 0 }}
        />
        <select
          value={selectedCategory}
          onChange={(e) => setSelectedCategory(e.target.value)}
          style={{ ...formInputStyle, maxWidth: "240px", marginTop: 0 }}
        >
          {categories.map((category) => (
            <option key={category} value={category}>
              {category === "ALL" ? "All Categories" : category}
            </option>
          ))}
        </select>
        <button onClick={fetchOrderItems} style={{ ...smallButtonStyle, background: "#f59e0b", color: "#111827" }}>
          My Orders
        </button>
      </div>

      {renderAddressForm()}
      {renderProductForm()}

      <div style={{ display: "flex", alignItems: "flex-start", gap: "18px" }}>
        <main style={{ flex: 1, minWidth: "280px" }}>
          {productsLoading ? (
            <p>Loading products...</p>
          ) : productsError ? (
            <p style={{ color: "#b91c1c", fontWeight: "bold" }}>{productsError}</p>
          ) : filteredProducts.length === 0 ? (
            <p>No products found.</p>
          ) : (
            <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(230px, 1fr))", gap: "18px" }}>
              {filteredProducts.map((product) => (
                <article
                  key={getProductId(product)}
                  style={{
                    border: "1px solid #e5e7eb",
                    borderRadius: "8px",
                    overflow: "hidden",
                    background: "#fff",
                    display: "flex",
                    flexDirection: "column",
                  }}
                >
                  <img
                    src={productImageSrc(product.productName)}
                    alt={product.productName}
                    style={{ width: "100%", height: "160px", objectFit: "cover", background: "#f3f4f6" }}
                  />
                  <div style={{ padding: "14px", display: "grid", gap: "8px", flex: 1 }}>
                    <h3 style={{ margin: 0 }}>{product.productName}</h3>
                    <p style={{ margin: 0, color: "#4b5563" }}>{product.description}</p>
                    <strong>Rs. {product.price}</strong>
                    <span>Category: {product.categoryType}</span>
                    <span>Stock: {product.stockQuantity}</span>
                    <div style={{ display: "flex", gap: "8px", marginTop: "auto" }}>
                      <button
                        onClick={() => buyNow(product)}
                        style={{ ...smallButtonStyle, background: "#4f46e5", color: "white", width: "100%" }}
                      >
                        Buy Now
                      </button>
                    </div>
                  </div>
                </article>
              ))}
            </div>
          )}
        </main>

        {renderOrdersPanel()}
      </div>
    </div>
  );

  const renderPlaceOrder = () => selectedProduct && (
    <div
      style={{
        width: "92%",
        maxWidth: "760px",
        margin: "40px auto",
        padding: "26px",
        background: "rgba(255,255,255,0.96)",
        borderRadius: "8px",
        boxShadow: "0 10px 30px rgba(0,0,0,0.2)",
      }}
    >
      <h1 style={{ marginTop: 0 }}>Place Order</h1>
      <div style={{ display: "grid", gridTemplateColumns: "180px 1fr", gap: "20px", alignItems: "start" }}>
        <img
          src={productImageSrc(selectedProduct.productName)}
          alt={selectedProduct.productName}
          style={{ width: "180px", height: "180px", objectFit: "cover", borderRadius: "8px" }}
        />
        <div>
          <h2 style={{ marginTop: 0 }}>{selectedProduct.productName}</h2>
          <p>{selectedProduct.description}</p>
          <p>Category: {selectedProduct.categoryType}</p>
          <p>Price: Rs. {selectedProduct.price}</p>
          <div
            style={{
              marginTop: "12px",
              padding: "12px",
              background: "#f8fafc",
              border: "1px solid #e5e7eb",
              borderRadius: "8px",
            }}
          >
            <strong>Delivery Address</strong>
            <p style={{ margin: "8px 0" }}>{hasAddress() ? getAddressText() : "No address added yet."}</p>
            <button onClick={() => setShowAddressForm(true)} style={{ ...smallButtonStyle, background: "#dbeafe" }}>
              {hasAddress() ? "Change Address" : "Add Address"}
            </button>
          </div>
          {renderAddressForm()}
          <input
            type="number"
            min="1"
            max={selectedProduct.stockQuantity || undefined}
            value={orderQuantity}
            onChange={(e) => setOrderQuantity(e.target.value)}
            style={{ ...formInputStyle, maxWidth: "180px" }}
          />
          <h3>Total: Rs. {Number(selectedProduct.price || 0) * Number(orderQuantity || 0)}</h3>
          <div style={{ display: "flex", gap: "10px", flexWrap: "wrap" }}>
            <button onClick={placeOrder} style={{ ...smallButtonStyle, background: "#16a34a", color: "white" }}>
              Place Order
            </button>
            <button onClick={() => setPage("customerDashboard")} style={{ ...smallButtonStyle, background: "#e5e7eb" }}>
              Back
            </button>
          </div>
        </div>
      </div>
    </div>
  );

  const renderOrderPayment = () => (
    <div
      style={{
        width: "92%",
        maxWidth: "560px",
        margin: "60px auto",
        padding: "26px",
        background: "rgba(255,255,255,0.96)",
        borderRadius: "8px",
        boxShadow: "0 10px 30px rgba(0,0,0,0.2)",
      }}
    >
      <h1 style={{ marginTop: 0 }}>Order Payment</h1>
      <p>Product: {selectedProduct?.productName}</p>
      <p>Quantity: {orderQuantity}</p>
      <h3>Total: Rs. {Number(selectedProduct?.price || 0) * Number(orderQuantity || 0)}</h3>
      <select value={paymentMode} onChange={(e) => setPaymentMode(e.target.value)} style={formInputStyle}>
        <option>UPI</option>
        <option>CARD</option>
        <option>CASH_ON_DELIVERY</option>
      </select>
      <button onClick={orderPayment} style={primaryButtonStyle}>
        Pay and Place Order
      </button>
    </div>
  );

  return (
    <div
      style={{
        minHeight: "100vh",
        backgroundImage: "url('/images/Inventory1.jpg')",
        backgroundSize: "cover",
        backgroundPosition: "left center",
        backgroundRepeat: "no-repeat",
        backgroundAttachment: "fixed",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      {page === "login" && (
        <div
          style={{
            display: "flex",
            justifyContent: "flex-end",
            alignItems: "center",
            minHeight: "100vh",
            width: "100%",
            paddingRight: "10px",
          }}
        >
          <div
            style={{
              marginLeft: "auto",
              marginRight: "80px",
              width: "400px",
              padding: "30px",
              background: "rgb(255, 249, 249)",
              backdropFilter: "blur(15px)",
              border: "1px solid rgba(255,255,255,0.3)",
              borderRadius: "20px",
              boxShadow: "0 10px 30px rgba(0,0,0,0.3)",
              textAlign: "center",
            }}
          >
            <img
              src="/images/LogIn.jpg"
              alt="login"
              style={{
                width: "140px",
                height: "140px",
                borderRadius: "50%",
                objectFit: "cover",
                marginBottom: "15px",
              }}
            />

            <h1>User Login</h1>

            <input
              type="email"
              placeholder="Email"
              value={emailId}
              onChange={(e) => setEmailId(e.target.value)}
              style={formInputStyle}
            />

            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={formInputStyle}
            />

            <button onClick={login} style={primaryButtonStyle}>
              Login
            </button>

            <p style={{ marginTop: "20px" }}>
              Don't have an account ?
              <span
                style={{
                  color: "#4f46e5",
                  cursor: "pointer",
                  fontWeight: "bold",
                  marginLeft: "5px",
                }}
                onClick={() => setPage("register")}
              >
                Register
              </span>
            </p>
          </div>
        </div>
      )}

      {page === "customerDashboard" && renderProducts()}
      {page === "vendorDashboard" && renderVendorDashboard()}
      {page === "adminDashboard" && renderAdminDashboard()}
      {page === "adminProducts" && renderAdminProducts()}
      {page === "adminOrderReport" && renderAdminOrderReport()}
      {page === "adminUsers" && renderAdminUsers()}
      {page === "adminRevenueReport" && renderAdminRevenueReport()}
      {page === "adminManageOrders" && renderAdminManageOrders()}
      {page === "managerDashboard" && renderManagerDashboard()}
      {page === "managerProducts" && renderManagerProducts()}
      {page === "managerOrderReport" && renderManagerOrderReport()}
      {page === "placeOrder" && renderPlaceOrder()}
      {page === "orderPayment" && renderOrderPayment()}

      {page === "register" && (
        <div
          style={{
            display: "flex",
            justifyContent: "flex-end",
            alignItems: "center",
            minHeight: "100vh",
            paddingRight: "70px",
            width: "100%",
          }}
        >
          <div
            style={{
              width: "420px",
              background: "#fff",
              padding: "30px",
              borderRadius: "20px",
              boxShadow: "0 10px 30px rgba(0,0,0,0.1)",
              textAlign: "center",
            }}
          >
            <img
              src="/images/Register.jpg"
              alt="register"
              style={{
                width: "120px",
                height: "130px",
                borderRadius: "50%",
                objectFit: "cover",
                marginBottom: "10px",
              }}
            />

            <h1>User Registration</h1>

            <input
              placeholder="User Name"
              value={userName}
              onChange={(e) => setUserName(e.target.value)}
              style={formInputStyle}
            />

            <input
              placeholder="Email"
              value={emailId}
              onChange={(e) => setEmailId(e.target.value)}
              style={formInputStyle}
            />

            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              style={formInputStyle}
            />

            <input
              placeholder="Phone Number"
              value={phoneNo}
              onChange={(e) => setPhoneNo(e.target.value)}
              style={formInputStyle}
            />

            <select value={role} onChange={(e) => setRole(e.target.value)} style={formInputStyle}>
              <option>CUSTOMER</option>
              <option>VENDOR</option>
              <option>MANAGER</option>
            </select>

            <button onClick={register} style={primaryButtonStyle}>
              Create Account
            </button>

            <p style={{ marginTop: "20px" }}>
              Already have an account ?
              <span
                style={{
                  color: "#4f46e5",
                  cursor: "pointer",
                  fontWeight: "bold",
                  marginLeft: "5px",
                }}
                onClick={() => setPage("login")}
              >
                Login
              </span>
            </p>
          </div>
        </div>
      )}
    </div>
  );
}

export default App;