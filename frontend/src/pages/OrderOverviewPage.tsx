import './OrderOverviewPage.css';
import axios from "axios";
import {useEffect, useState} from "react";
import {Order} from "../types/Order.ts";
import {DataGrid, GridColDef, GridRenderCellParams} from '@mui/x-data-grid';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import EditIcon from '@mui/icons-material/Edit';
import {Link} from "react-router-dom";

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

	// Fetch orders and total quantities on first render
	useEffect(() => {
		getOrders();
		getTotalQuantitySmoked();
		getTotalQuantityFresh();
	}, []);

	// Function to refresh the orders and total quantities
	const handleRefresh = () => {
		getOrders();
		getTotalQuantitySmoked();
		getTotalQuantityFresh();
	};

	// Function to search orders by lastname, email or id
	const searchOrders = (): Order[] => {
		return orders?.filter(order => {
			const lowerCaseSearch = search.toLowerCase();
			return order.lastname.toLowerCase().includes(lowerCaseSearch) ||
				order.email.toLowerCase().includes(lowerCaseSearch) ||
				(order.id ?? ``).toLowerCase().includes(lowerCaseSearch);
		}) || [];
	}

	// Columns for the DataGrid
	const columns: GridColDef[] = [
		{field: 'id', headerName: 'Bestellnummer', width: 150},
		{field: 'lastname', headerName: 'Nachname', width: 200},
		{field: 'firstname', headerName: 'Vorname', width: 200},
		{field: 'email', headerName: 'E-Mail', width: 250},
		{field: 'pickupPlace', headerName: 'Abholort', width: 120},
		{field: 'comment', headerName: 'Kommentar', width: 200},
		{field: 'quantitySmoked', headerName: 'Geräucherte', width: 100},
		{field: 'quantityFresh', headerName: 'Eingelegte', width: 100},
		{
			field: 'totalPrice',
			headerName: 'Gesamtbetrag',
			width: 150,
			renderCell: (params: GridRenderCellParams) => (params.row.quantitySmoked * 7.5 + params.row.quantityFresh * 6).toFixed(2) + '€'
		},
		{field: 'editRemove', headerName: '', width: 70, renderCell: (params: GridRenderCellParams) => (<div className="editRemove"><button onClick={()=> handleDelete(params.row.id)}><DeleteForeverIcon fontSize="large"/></button> <Link to={`/order-edit/${params.row.id}`}><EditIcon fontSize="large"/></Link></div>)}
	];
	// Functions to edit and delete orders
	const handleDelete = (id: string) => {
		axios.delete(`/api/orders/${id}`)
			.then(() => {
				handleRefresh();
			})
			.catch(error => {
				console.error(error);
			});
	}

	// Return the page
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
			<div className="searchContainer">
				<Link to="/">zurück</Link>
			<input className="search" type="search" placeholder="Suche..."
			       onChange={event => setSearch(event.target.value)}/>
			</div>
			<DataGrid rows={searchOrders()} columns={columns} getRowId={(row) => row.id}
			          initialState={{
				          pagination: {
					          paginationModel: {
						          pageSize: 20,
					          },
				          },
			          }} sx={{fontSize: '1.4rem', borderColor: 'white', width:'90vw'}}/>
		</div>
	)
}