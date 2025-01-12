import { Button, Paper } from '@mui/material'
import {
	DataGrid,
	GridRenderCellParams,
	GridValueFormatterParams,
} from '@mui/x-data-grid'
import { listEnrichmentRequests } from 'api/enrichment-api'
import { EnrichmentRequest } from 'models/enrichment-request/enrichment-request'
import { EnrichmentRequestFilterDto } from 'models/enrichment-request/enrichment-request-filter-dto'
import { JobPlan } from 'models/job-plan'
import { FC, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { dateTimeFormatter, ownerFormatter } from 'utils/formatters'

export const publicationCountFormatter = (params: GridValueFormatterParams) => {
	if (params.value === undefined) {
		return '-'
	}

	const jobPlans = params.value as JobPlan[]
	return jobPlans.length.toString()
}

export const EnrichmentRequestList: FC<{
	filter: EnrichmentRequestFilterDto
}> = ({ filter }) => {
	const [rowCount, setRowCount] = useState<number>()
	const [enrichmentRequests, setEnrichmentRequests] = useState<
		EnrichmentRequest[]
	>([])
	const [page, setPage] = useState<number>(0)
	const [rowCountState, setRowCountState] = useState<number | undefined>(
		rowCount,
	)
	const navigate = useNavigate()

	const columns = [
		{
			field: 'id',
			headerName: 'ID',
			width: 400,
			type: 'string',
		},
		{
			field: 'created',
			headerName: 'Vytvořeno',
			width: 250,
			type: 'string',
			valueFormatter: dateTimeFormatter,
		},
		{
			field: 'owner',
			headerName: 'Vytvořil',
			width: 250,
			type: 'string',
			valueFormatter: ownerFormatter,
		},
		{
			field: 'name',
			headerName: 'Název',
			width: 350,
			type: 'string',
		},
		{
			field: 'jobPlans',
			headerName: 'Počet publikací',
			width: 200,
			type: 'string',
			valueFormatter: publicationCountFormatter,
		},
		{
			field: 'action',
			headerName: 'Akce',
			flex: 1,
			sortable: false,
			renderCell: (params: GridRenderCellParams) => {
				const onClick = () => {
					navigate(params.row['id'])
				}
				return (
					<Button color="primary" variant="contained" onClick={onClick}>
						Detail
					</Button>
				)
			},
		},
	]

	useEffect(() => {
		async function fetchRequests() {
			const response = await listEnrichmentRequests(page, 10, filter)

			if (response) {
				setEnrichmentRequests(response.results)
				setRowCount(response.limit)
			}
		}
		fetchRequests()
	}, [page, filter])

	useEffect(() => {
		setRowCountState(prevRowCountState =>
			rowCount !== undefined ? rowCount : prevRowCountState,
		)
	}, [rowCount, setRowCountState])

	const onPageChange = (page: number) => setPage(page)

	return (
		<Paper>
			<DataGrid
				autoHeight
				columns={columns}
				density="compact"
				disableColumnFilter
				disableColumnMenu
				disableSelectionOnClick
				pageSize={10}
				paginationMode="server"
				rowCount={rowCountState}
				rows={enrichmentRequests}
				rowsPerPageOptions={[]}
				onPageChange={onPageChange}
			/>
		</Paper>
	)
}
