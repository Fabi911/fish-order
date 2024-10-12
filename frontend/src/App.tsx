import './App.css';
import OrderPage from "./pages/OrderPage.tsx";
import OrderOverviewPage from "./pages/OrderOverviewPage.tsx";
import {Route, Routes} from "react-router-dom";

function App() {
	return (
		<>
			<img src='./logo.png' alt="fv-leineck" width="150"/>
			<Routes>
				<Route path="/" element={<OrderPage/>}/>
				<Route path="/order-overview" element={<OrderOverviewPage/>}/>
			</Routes>
		</>
	)
}

export default App