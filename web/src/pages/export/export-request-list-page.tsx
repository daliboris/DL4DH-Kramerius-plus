import { Grid } from '@mui/material'
import { ExportRequestFilterDto } from 'models/export-request/export-request-filter-dto'
import { ExportRequestFilter } from 'modules/export-request/export-request-filter'
import { ExportRequestList } from 'modules/export-request/export-request-list'
import { PageWrapper } from 'pages/page-wrapper'
import { FC, useState } from 'react'

export const ExportRequestListPage: FC = () => {
	const [filter, setFilter] = useState<ExportRequestFilterDto>({})

	const onFilterSubmit = (filter: ExportRequestFilterDto) => {
		setFilter({ ...filter })
	}

	return (
		<PageWrapper requireAuth>
			<Grid container direction="column" spacing={3}>
				<Grid item xs={12}>
					<ExportRequestFilter onSubmit={onFilterSubmit} />
				</Grid>
				<Grid item xs={12}>
					<ExportRequestList filter={filter} />
				</Grid>
			</Grid>
		</PageWrapper>
	)
}
