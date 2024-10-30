import './App.css';
import OrderPage from "./pages/OrderPage.tsx";
import OrderOverviewPage from "./pages/OrderOverviewPage.tsx";
import {Route, Routes, useNavigate} from "react-router-dom";
import EditPage from "./pages/EditPage.tsx";
import axios from "axios";
import {useEffect, useState} from "react";
import {Order} from "./types/Order.ts";
import {AppUser} from "./types/AppUser.ts";
import LoginPage from "./pages/LoginPage.tsx";
import RegisterPage from "./pages/RegisterPage.tsx";

function App() {
	const [orders, setOrders] = useState<Order[]>([]);
	const [appUser, setAppUser] = useState<AppUser | null>(null);
	const navigate = useNavigate();

	function login(username: string, password: string) {
		axios.post("/api/users/login", {}, {
			auth: {
				username: username,
				password: password
			}
		})
			.then(() => {
				console.log("Login successful");
				fetchMe();
				navigate("/order-overview");
			})
			.catch(e => {
				setAppUser(null);
				console.error(e)
			});
	}

	function logout() {
		axios.post("/api/users/logout")
			.then(() => {
				console.log("Logout successful")
				navigate("/");
			})
			.catch(e => console.error(e))
			.finally(() => setAppUser(null));
	}

	async function fetchMe() {
		try {
			const res = await axios.get("/api/users/me");
			if (res.data && typeof res.data === 'object' && 'id' in res.data && 'username' in res.data && 'role' in res.data) {
				setAppUser(res.data as AppUser);
			} else {
				throw new Error("Unexpected response structure");
			}
		} catch (e) {
			console.error("Error fetching user:", e);
			setAppUser(null);
		}
	}

	// fetch orders from backend
	const fetchOrders = () => {
		axios.get('/api/orders')
			.then(response => {
				setOrders(response.data);
			})
			.catch(error => console.error(error));
	}
	useEffect(() => {
		fetchOrders();
		fetchMe();
	}, []);
	const isAuthorizedAdminGroup = appUser?.role === "ADMIN" || appUser?.role === "GROUP1";
	return (
		<>
			<img src='./logo.png' alt="fv-leineck" width="150"/>
			{appUser && <button className="logout-button" onClick={logout}>Logout</button>}
			<Routes>
				<Route path="/" element={<OrderPage appUser={appUser}/>}/>
				<Route path="/login" element={<LoginPage login={login}/>}/>
				<Route path="/register" element={<RegisterPage/>}/>
				{appUser && isAuthorizedAdminGroup && (
					<>
						<Route path="/order-overview" element={<OrderOverviewPage appUser={appUser}/>}/>
						<Route path="/order-edit/:id" element={<EditPage orders={orders}/>}/>
					</>
				)}
			</Routes>
		</>
	)
}

export default App