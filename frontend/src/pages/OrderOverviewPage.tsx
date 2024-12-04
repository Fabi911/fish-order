import './OrderOverviewPage.css';
import axios from "axios";
import {useEffect, useState} from "react";
import {Order} from "../types/Order.ts";
import {DataGrid, GridColDef, GridRenderCellParams} from '@mui/x-data-grid';
import DeleteForeverIcon from '@mui/icons-material/DeleteForever';
import EditIcon from '@mui/icons-material/Edit';
import RefreshIcon from '@mui/icons-material/Refresh';
import CheckCircleIcon from '@mui/icons-material/CheckCircle';
import ArrowCircleUpIcon from '@mui/icons-material/ArrowCircleUp';
import {Link} from "react-router-dom";
import ExportToXLSX from "../components/ExportToXLSX.tsx";
import {AppUser} from "../types/AppUser.ts";
import '@mui/x-data-grid';
import {toast} from "react-toastify";

declare module '@mui/material/styles' {
	interface Components {
		MuiDataGrid?: {
			styleOverrides?: {
				[key: string]: object;
			};
		};
	}
}
export default function OrderOverviewPage({appUser}: { appUser: AppUser }) {
	const [orders, setOrders] = useState<Order[] | null>(null);
	const [totalSmoked, setTotalSmoked] = useState<number>(0);
	const [totalFresh, setTotalFresh] = useState<number>(0);
	const [search, setSearch] = useState<string>('');
	// Function to get all orders from the backend
	const getOrders = async () => {
		try {
			const response = await axios.get('/api/orders');
			setOrders(response.data);
		} catch (error) {
			if (axios.isAxiosError(error) && error.response?.status === 500) {
				console.error("Internal Server Error: Unable to fetch orders.");
				// Display a user-friendly message or take appropriate action
				alert("An error occurred while fetching orders. Please try again later.");
			} else {
				console.error(error);
			}
		}
	};
	// Function to get total quantity from the backend
	const getTotalQuantitySmoked = async () => {
		try {
			const response = await axios.get('/api/orders/smoked');
			setTotalSmoked(response.data);
		} catch (error) {
			console.error(error);
		}
	}
	const getTotalQuantityFresh = async () => {
		try {
			const response = await axios.get('/api/orders/fresh');
			setTotalFresh(response.data);
		} catch (error) {
			console.error(error);
		}
	}
	// Fetch orders and total quantities on first render
	useEffect(() => {
		getOrders();
		getTotalQuantitySmoked();
		getTotalQuantityFresh();
	}, [appUser]);
	// Function to refresh the orders and total quantities
	const handleRefresh = () => {
		getOrders();
		getTotalQuantitySmoked();
		getTotalQuantityFresh();
	};

	// Function to filter orders by status picked up
	function openOrders() {
		return orders?.filter(order => !order.pickedUp);
	}

	function closedOrders() {
		return orders?.filter(order => order.pickedUp) || [];
	}

	const closedOrdersList = closedOrders();
	// Function to search orders by lastname, email or id
	const searchOrders = (): Order[] => {
		return openOrders()?.filter(order => {
			const lowerCaseSearch = search.toLowerCase();
			return order.lastname.toLowerCase().includes(lowerCaseSearch) ||
				order.email.toLowerCase().includes(lowerCaseSearch) ||
				(order.id ?? ``).toLowerCase().includes(lowerCaseSearch);
		}) || [];
	}
	// Columns for the DataGrid
	const columns: GridColDef[] = [
		{
			field: 'pickedUp',
			headerName: '',
			width: 35,
			renderCell: (params: GridRenderCellParams) => (
				<button onClick={() => togglePickedUp(params.row)}>{params.row.pickedUp? <ArrowCircleUpIcon className="icons cancel" fontSize="large"/> :<CheckCircleIcon className="icons pickup" fontSize="large"/>}</button>)
		},
		{field: 'id', headerName: 'Bestellnummer', width: 150},
		{field: 'lastname', headerName: 'Nachname', width: 200},
		{field: 'firstname', headerName: 'Vorname', width: 150},
		{field: 'email', headerName: 'E-Mail', width: 250},
		{field: 'phone', headerName: 'Telefonnummer', width: 200},
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
		{
			field: 'editRemove',
			headerName: '',
			width: 70,
			renderCell: (params: GridRenderCellParams) => (<div className="editRemove">
				{appUser && appUser.role === "ADMIN" &&
					<button onClick={() => handleDelete(params.row)}><DeleteForeverIcon className="icons delete"
					                                                                    fontSize="large"/></button>}
				<Link to={`/order-edit/${params.row.id}`}><EditIcon className="icons edit" fontSize="large"/></Link>
			</div>)
		}
	];
	// Functions to edit and delete orders
	const handleDelete = (order: Order) => {
		axios.delete(`/api/orders/${order.id}`)
			.then(() => {
				handleRefresh();
				toast.success("Bestellung erfolgreich gelöscht!");
			})
			.catch(error => {
				console.error(error);
			});
	}
	// Function to toggle the pickedUp status of an order
	const togglePickedUp = (order: Order) => {
		axios.put(`/api/orders/${order.id}`, {
			firstname: order.firstname,
			lastname: order.lastname,
			email: order.email,
			phone: order.phone,
			pickupPlace: order.pickupPlace,
			comment: order.comment,
			quantitySmoked: order.quantitySmoked,
			quantityFresh: order.quantityFresh,
			pickedUp: !order.pickedUp
		})
			.then(() => {
				handleRefresh();
			})
			.catch(error => {
				console.error(error);
			});
	}
// Return loading message if orders are not loaded yet
	if (!orders) {
		return <h1>Lade...</h1>
	}
// Return the page
	return (
		<div className="pageContainer">
			<h1 className="headline-overview">Bestellübersicht</h1>
			<article className="quantityBox">
				<h2>Gesamtmenge</h2>
				<p>Geräucherte Forellen: <b>{totalSmoked}</b></p>
				<p>Eingelegte Forellen: <b>{totalFresh}</b></p>
				<p>Gesamt: <b>{totalSmoked + totalFresh}</b></p>
			</article>
			<div className="searchContainer">
				<Link className="exportButton link" to="/">zum Bestellformular</Link>
				<input className="search" type="search" placeholder="Suche..."
				       onChange={event => setSearch(event.target.value)}/>
				<ExportToXLSX data={orders} totalSmoked={totalSmoked} totalFresh={totalFresh}/>
				<button className="exportButton" onClick={handleRefresh}><RefreshIcon fontSize="large"/></button>

			</div>
			<DataGrid
				rows={searchOrders()}
				columns={columns}
				getRowId={(row) => row.id}
				initialState={{
					pagination: {
						paginationModel: {
							pageSize: 15,
						},
					},
				}}
				sx={{
					fontSize: '1.4rem',
					width: '100%',
					'--DataGrid-containerBackground': 'transparent!important',
					'& .MuiDataGrid-row': {
						backgroundColor: 'white',
						'&:hover': {
							backgroundColor: '#bbbb9b',
						}
					},
					'& .MuiDataGrid-row.Mui-selected': {
						backgroundColor: '#FAFAD2',
						'&:hover': {
							backgroundColor: '#bbbb9b',
						}
					},
					'& .MuiDataGrid-columnHeaders': {
						backgroundColor: '#e5e5db !important',
						color: 'black',
						fontSize: '1.4rem',
					},
					'& .MuiDataGrid-columnHeaderTitle': {
						fontWeight: 'bold',
					},
					'& .MuiDataGrid-columnHeader': {
						borderBottom: '1px solid red',
					},
					'& .MuiDataGrid-footerContainer': {
						backgroundColor: '#e5e5db',
					},
					'& .MuiTablePagination-displayedRows': {
						fontSize: '1.4rem',
					},
					'& .MuiButtonBase-root.Mui-disabled': {
						fontSize: '1.4rem',
						color: 'black',
					},
					'& .MuiTablePagination-actions': {
						cursor: 'pointer',
					},
				}}
			/>


			{closedOrdersList.length > 0 &&
				<>
					<h3 className={"closedOrders"}>Abgeschlossene Bestellungen</h3>
					<DataGrid
						rows={closedOrders()}
						columns={columns}
						getRowId={(row) => row.id}
						initialState={{
							pagination: {
								paginationModel: {
									pageSize: 15,
								},
							},
						}}
						sx={{
							fontSize: '1.4rem',
							width: '100%',
							'--DataGrid-containerBackground': 'transparent!important',
							'& .MuiDataGrid-row': {
								backgroundColor: 'white',
								'&:hover': {
									backgroundColor: '#bbbb9b',
								}
							},
							'& .MuiDataGrid-row.Mui-selected': {
								backgroundColor: '#FAFAD2',
								'&:hover': {
									backgroundColor: '#bbbb9b',
								}
							},
							'& .MuiDataGrid-columnHeaders': {
								backgroundColor: '#e5e5db !important',
								color: 'black',
								fontSize: '1.4rem',
							},
							'& .MuiDataGrid-columnHeaderTitle': {
								fontWeight: 'bold',
							},
							'& .MuiDataGrid-columnHeader': {
								borderBottom: '1px solid red',
							},
							'& .MuiDataGrid-footerContainer': {
								backgroundColor: '#e5e5db',
							},
							'& .MuiTablePagination-displayedRows': {
								fontSize: '1.4rem',
							},
							'& .MuiButtonBase-root.Mui-disabled': {
								fontSize: '1.4rem',
								color: 'black',
							},
							'& .MuiTablePagination-actions': {
								cursor: 'pointer',
							},
						}}
					/>
				</>
			}
		</div>
	)
}