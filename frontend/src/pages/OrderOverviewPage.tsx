import './OrderOverviewPage.css';
import axios from "axios";
import {useEffect, useState} from "react";
import {Order} from "../types/Order.ts";

export default function OrderOverviewPage() {
	const [orders, setOrders] = useState<Order[] | null>(null);
	const[totalSmoked, setTotalSmoked] = useState<number>(0);
	const[totalFresh, setTotalFresh] = useState<number>(0);

	// Function to get all orders from the backend
		const getOrders = async () => {
		axios.get('/api/orders')
			.then(response => {
				setOrders(response.data);
			})
			.catch(error => {
				console.error(error);
			});
	}

	// Function to get total quantity from the backend
	const   getTotalQuantitySmoked = async () => {
			await axios.get('/api/orders/smoked')
			.then(response => {
				setTotalSmoked(response.data);
			})
			.catch(error => {
				console.error(error);
			});
	}

	const   getTotalQuantityFresh = async () => {
			await axios.get('/api/orders/fresh')
			.then(response => {
				setTotalFresh(response.data);
			})
			.catch(error => {
				console.error(error);
			});
	}

	useEffect(() => {
		getOrders();
		getTotalQuantitySmoked();
		getTotalQuantityFresh();
	}, []);

	const handleRefresh = () => {
		getOrders();
		getTotalQuantitySmoked();
		getTotalQuantityFresh();
	};

	return (
		<>
			<h1>Übersicht</h1>
			<button onClick={handleRefresh}>Refresh</button>
			<h2>Gesamtmenge</h2>
			<p>Geräucherte Forellen: {totalSmoked}</p>
			<p>Eingelegte Forellen: {totalFresh}</p>
			<h2>Bestellungen</h2>
			{orders && (
				<table>
					<thead>
					<tr>
						<th>Bestellnummer</th>
						<th>Nachname</th>
						<th>Vorname</th>
						<th>E-Mail Adresse</th>
						<th>geräucherte Forellen</th>
						<th>eingelegte Forellen</th>
						<th>Gesamtbetrag</th>
					</tr>
					</thead>
					<tbody>
					{orders.map(order => (
						<tr key={order.id}>
							<td>{order.id}</td>
							<td>{order.lastname}</td>
							<td>{order.firstname}</td>
							<td>{order.email}</td>
							<td>{order.quantitySmoked}</td>
							<td>{order.quantityFresh}</td>
							<td>{(order.quantitySmoked * 8 + order.quantityFresh * 6.50).toFixed(2)} €</td>
						</tr>
					))}
					</tbody>
				</table>
			)}
		</>
	)
}