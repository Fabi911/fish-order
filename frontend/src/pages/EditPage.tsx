import './OrderPage.css';
import {Order} from "../types/Order.ts";
import EditForm from "../components/EditForm.tsx";
import {useParams} from "react-router-dom";

interface EditPageProps {
	orders: Order[];
}

export default function EditPage({ orders }: EditPageProps) {
	const {id} = useParams();
	const order = orders.find(order => order.id === id);
	return (
		<>
			<h1>Bestellung Forellenverkauf</h1>
			<article className="article">
				<h2>für den 22.12.2024</h2>
				<h3>Preise:</h3>
				<p className="price">geräucherte Forelle: 7,50€</p>
				<p>eingelegte Forelle: 6,00€</p>
			</article>
			<EditForm order={order} />
		</>
	)
}