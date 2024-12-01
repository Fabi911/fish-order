import './OrderPage.css';
import {Order} from "../types/Order.ts";
import EditForm from "../components/EditForm.tsx";
import {Link, useParams} from "react-router-dom";
import axios from "axios";
import {useEffect, useState} from "react";



export default function EditPage() {
	const {id} = useParams();
	const [orders, setOrders] = useState<Order[] | undefined>([]);
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
	}, []);
	if (!orders) {
		return <h1>Bestellung nicht gefunden</h1>
	}
	const order = orders.find(order => order.id === id);

	if (!order) {
		return <h1>Bestellung nicht gefunden</h1>
	}

	return (
		<>
			<h1>Bestellung Forellenverkauf</h1>
			<article className="article">
				<h2>für den 22.12.2024</h2>
				<h3>Preise:</h3>
				<p className="price">geräucherte Forelle: 7,50€</p>
				<p>eingelegte Forelle: 6,00€</p>
			</article>
			<EditForm order={order}/>
			<br/>
			<Link to="/order-overview">Zur Übersicht</Link>
		</>
	)
}