import React from 'react'
import Typography from '@material-ui/core/Typography'
import Grid from '@material-ui/core/Grid'
import { makeStyles } from '@material-ui/core/styles'

import { Params, Sort } from '../../models'
import { CheckboxField } from '../checkbox-field/checkbox-field'
import { TextField } from '../text-field/text-field'
import { SelectField } from '../select-field/select-field'
import { fieldOptions, sortOptions } from './publication-items'

type Props = {
	params: Params
	setParams: React.Dispatch<React.SetStateAction<Params>>
}

const useStyles = makeStyles(() => ({
	root: {
		marginTop: 20,
		marginBottom: 10,
		width: 400,
	},
}))

export const JSONParams = ({ setParams, params }: Props) => {
	const classes = useStyles()
	const {
		disablePagination = false,
		pageOffset = 0,
		pageSize = 20,
		sort = [],
		includeFields = [],
	} = params

	return (
		<div className={classes.root}>
			<Typography>Parametry</Typography>

			<Grid container>
				<CheckboxField
					label="Zakázat stránkování"
					name="disablePagination"
					value={disablePagination}
					onChange={v => setParams(p => ({ ...p, disablePagination: v }))}
				/>
			</Grid>

			<Grid container spacing={2}>
				<Grid item xs={6}>
					<TextField
						disabled={disablePagination}
						label="Stránka"
						name="pageOffset"
						type="number"
						value={pageOffset}
						onChange={v => setParams(p => ({ ...p, pageOffset: Number(v) }))}
					/>
				</Grid>

				<Grid item xs={6}>
					<TextField
						disabled={disablePagination}
						label="Záznamy na stránku"
						name="pageSize"
						type="number"
						value={pageSize}
						onChange={v => setParams(p => ({ ...p, pageSize: Number(v) }))}
					/>
				</Grid>
			</Grid>

			<Grid container spacing={2}>
				<Grid item xs={6}>
					<SelectField<{ id: string; label: string }>
						items={fieldOptions}
						label="Zahrnout pole"
						multiple
						name="includeFields"
						optionMapper={o => o.id}
						value={includeFields}
						onChange={v =>
							setParams(p => ({
								...p,
								includeFields: v as string[],
							}))
						}
					/>
				</Grid>
				<Grid item xs={6}>
					<SelectField<Sort>
						items={sortOptions}
						label="Sort"
						labelMapper={o => o.direction}
						name="sort"
						optionMapper={o => o.direction}
						value={sort}
						onChange={v =>
							setParams(p => ({
								...p,
								sort: v as Sort,
							}))
						}
					/>
				</Grid>
			</Grid>
			{/* <ExportFilters filters={filters} setParams={setParams} /> */}
		</div>
	)
}
