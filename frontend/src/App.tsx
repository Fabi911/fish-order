import './App.css';
import OrderPage from "./pages/OrderPage.tsx";
import OrderOverviewPage from "./pages/OrderOverviewPage.tsx";
import {Route, Routes} from "react-router-dom";
import EditPage from "./pages/EditPage.tsx";
import axios from "axios";
import {useEffect, useState} from "react";
import {Order} from "./types/Order.ts";

function App() {
	const [orders, setOrders] = useState <Order[]>([]);
	const fetchOrders = () => {
		axios.get('/api/orders')
			.then(response => {
				setOrders(response.data);
			})
			.catch(error => console.error(error));
	}

	useEffect(() => {
		fetchOrders();
	}, []);

	return (
		<>
			<img src='./logo.png' alt="fv-leineck" width="150"/>
			<Routes>
				<Route path="/" element={<OrderPage/>}/>
				<Route path="/order-overview" element={<OrderOverviewPage/>}/>
				<Route path="/order-edit/:id" element={<EditPage orders={orders}/>}/>
			</Routes>
		</>
	)
}

export default App