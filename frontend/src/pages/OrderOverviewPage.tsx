import './OrderOverviewPage.css';
import axios from "axios";
import {useEffect, useState} from "react";
import {Order} from "../types/Order.ts";
import {DataGrid, GridColDef, GridRenderCellParams} from '@mui/x-data-grid';

export default function OrderOverviewPage() {
	const [orders, setOrders] = useState<Order[] | null>(null);
	const [totalSmoked, setTotalSmoked] = useState<number>(0);
	const [totalFresh, setTotalFresh] = useState<number>(0);
	const [search, setSearch] = useState<string>('');
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
	const getTotalQuantitySmoked = async () => {
		await axios.get('/api/orders/smoked')
			.then(response => {
				setTotalSmoked(response.data);
			})
			.catch(error => {
				console.error(error);
			});
	}
	const getTotalQuantityFresh = async () => {
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
	const searchOrders = (): Order[] => {
		return orders?.filter(order => {
			const lowerCaseSearch = search.toLowerCase();
			return order.lastname.toLowerCase().includes(lowerCaseSearch) ||
				order.email.toLowerCase().includes(lowerCaseSearch) ||
				(order.id ?? ``).toLowerCase().includes(lowerCaseSearch);
		}) || [];
	}
	const columns: GridColDef[] = [
		{field: 'id', headerName: 'Bestellnummer', width: 150},
		{field: 'lastname', headerName: 'Nachname', width: 200},
		{field: 'firstname', headerName: 'Vorname', width: 200},
		{field: 'email', headerName: 'E-Mail', width: 250},
		{field: 'pickupPlace', headerName: 'Abholort', width: 200},
		{field: 'comment', headerName: 'Kommentar', width: 200},
		{field: 'quantitySmoked', headerName: 'Geräucherte', width: 100},
		{field: 'quantityFresh', headerName: 'Eingelegte', width: 100},
		{
			field: 'totalPrice',
			headerName: 'Gesamtbetrag',
			width: 150,
			renderCell: (params: GridRenderCellParams) => (params.row.quantitySmoked * 8 + params.row.quantityFresh * 6.5).toFixed(2) + '€'
		}
	];
	return (
		<div className="pageContainer">
			<h1>Übersicht</h1>
			<button onClick={handleRefresh}>Refresh</button>
			<article className="quantityBox">
				<h2>Gesamtmenge</h2>
				<p>Geräucherte Forellen: <b>{totalSmoked}</b></p>
				<p>Eingelegte Forellen: <b>{totalFresh}</b></p>
				<p>Gesamt: <b>{totalSmoked + totalFresh}</b></p>
			</article>
			<h2>Bestellungen</h2>
			<input className="search" type="search" placeholder="Suche..."
			       onChange={event => setSearch(event.target.value)}/>
			<DataGrid rows={searchOrders()} columns={columns} autoHeight={true} getRowId={(row) => row.id}
			          initialState={{
				          pagination: {
					          paginationModel: {
						          pageSize: 20,
					          },
				          },
			          }} sx={{fontSize: '1.4rem', borderColor: 'white'}}/>
		</div>
	)
}