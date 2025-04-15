import ExcelJS from "exceljs";
import {Order} from "../types/Order.ts";
import FileDownloadIcon from '@mui/icons-material/FileDownload';
import '../pages/OrderOverviewPage.css';

interface Props {
	data: Order[];
	totalSmoked: number;
	totalFresh: number;
}
export default function ExportToXLSX ({data, totalSmoked, totalFresh}: Props){
	async function  handleExport(data: Order[]) {
		const workbook = new ExcelJS.Workbook();
		const worksheet = workbook.addWorksheet('Bestellungen');
		// Füge die Kopfzeile hinzu
		worksheet.columns = [
			{header: 'Bestellnummer', key: 'id', width: 20},
			{header: 'Nachname', key: 'lastname', width: 20},
			{header: 'Vorname', key: 'firstname', width: 20},
			{header: 'E-Mail', key: 'email', width: 35},
			{header: 'Telefonnummer', key: 'phone', width: 25},
			/*{header: 'Abholort', key: 'pickupPlace', width: 20},*/
			{header: 'Kommentar', key: 'comment', width: 35},
			{header: 'Geräucherte', key: 'quantitySmoked', width: 15},
			{header: 'Eingelegte', key: 'quantityFresh', width: 15},
			{header: 'Gesamtbetrag', key: 'totalPrice', width: 20}
		];
		// Füge die Daten hinzu
		data.forEach((order) => {
			const row = worksheet.addRow(order);
			row.font = {name: 'Arial', size: 14};
		});
		for (let i = 2; i <= data.length + 1; i++) {
			worksheet.getCell(`J${i}`).value = {
				formula: `SUM(H${i}*7.5+I${i}*6)`,
				result: (totalSmoked * 7.5 + totalFresh * 6).toFixed(2) + '€'
			}
		}
		;
		// Stile für die Kopfzeile hinzufügen
		worksheet.getRow(1).font = {bold: true, size: 16, color: {argb: '000000'}};
		// Anwenden eines Filters auf die Kopfzeile
		worksheet.views = [{showGridLines: true, zoomScale: 100}];
		worksheet.autoFilter = {
			from: 'A1',
			to: 'I1'
		};
		// Exportiere die Datei
		const buffer = await workbook.xlsx.writeBuffer();
		const blob = new Blob([buffer], {type: 'application/octet-stream'});
		const url = window.URL.createObjectURL(blob);
		const a = document.createElement('a');
		a.href = url;
		a.download = 'bestellungen.xlsx';
		a.click();
		window.URL.revokeObjectURL(url);
	}
return (
	<>
	<button className="exportButton" onClick={() => handleExport(data)} ><FileDownloadIcon fontSize="large"/> Download Excel</button>
	</>
)
};