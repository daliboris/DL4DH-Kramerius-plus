import { useState, useMemo, useContext } from 'react'
import { v4 } from 'uuid'
import Grid from '@material-ui/core/Grid'
import TextField from '@material-ui/core/TextField'
import Button from '@material-ui/core/Button'
import IconButton from '@material-ui/core/IconButton'
import AddCircleOutlineIcon from '@material-ui/icons/AddCircleOutline'
import Paper from '@material-ui/core/Paper'
import InputAdornment from '@material-ui/core/InputAdornment'
import Typography from '@material-ui/core/Typography'
import RemoveCircleOutlineIcon from '@material-ui/icons/RemoveCircleOutline'
import { makeStyles } from '@material-ui/core/styles'
import { toast } from 'react-toastify'

import { ApiError } from 'models'

import { DialogContext } from 'components/dialog/dialog-context'

import { enrich } from './enrichment-api'
import { DefaultDialog } from 'components/dialog/knav-dialog/knav-default-dialog'

const useStyles = makeStyles(() => ({
	addButton: {
		padding: '2px 6px',
		textTransform: 'none',
	},
	paper: {
		padding: '10px 24px',
		minHeight: 140,
	},
	input: {
		'& input': {
			padding: '8px 10px',
		},
		'& > div': {
			paddingRight: 8,
		},
		marginBottom: 8,
	},
	formTitle: {
		marginBottom: 10,
	},
	submitButton: {
		display: 'flex',
		justifyContent: 'flex-end',
		alignItems: 'flex-start',
	},
}))

type Fields = {
	id: string
	value: string
}

const initialValue = { id: v4(), value: '' }

export const EnrichmentForm = () => {
	const classes = useStyles()
	const [idFields, setIdFields] = useState<Fields[]>([initialValue])
	const { open } = useContext(DialogContext)

	const disabledSubmitButton = useMemo(() => {
		const foundEmptyValueIndex = idFields.findIndex(f => !f.value)

		return foundEmptyValueIndex !== -1
	}, [idFields])

	// adds field
	const addField = () =>
		setIdFields(idFields => [...idFields, { id: v4(), value: '' }])

	// removes field
	const removeField = (id: string) => {
		const newFields = idFields.filter(f => f.id !== id)
		setIdFields(newFields)
	}

	const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
		e.preventDefault()

		const publications = idFields.map(f => f.value.trim())

		const response = await enrich(publications, false)

		if (response.ok) {
			toast('Operace proběhla úspěšně', {
				type: 'success',
			})

			setIdFields([initialValue])
		} else {
			const { code } = response.data as ApiError

			if (code === 'ALREADY_ENRICHED') {
				open({
					Content: () => (
						<DefaultDialog
							contentHeight={25}
							title="Opakované obohacení"
							onSubmit={async () => {
								const response = await enrich(publications, true)

								if (response.ok) {
									toast('Opakované obohacení proběhlo úspěšně', {
										type: 'success',
									})

									setIdFields([initialValue])
								}
							}}
						>
							Jedna nebo více publikací již existuje. Přejete si je obohatit
							znovu?
						</DefaultDialog>
					),
					size: 'md',
				})
			} else {
				toast('Při pokusu o obohacení nastala chyba.', {
					type: 'error',
				})
			}
		}
	}

	const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		const newFields = idFields.map(f => {
			if (f.id === e.target.name) {
				return {
					...f,
					value: e.target.value,
				}
			}

			return f
		})

		setIdFields(newFields)
	}

	return (
		<Paper className={classes.paper}>
			<form onSubmit={handleSubmit}>
				<div className={classes.formTitle}>
					<Typography color="primary" variant="h6">
						UUID publikací:
					</Typography>
				</div>
				<Grid container justifyContent="space-between" spacing={2}>
					<Grid item xs={9}>
						{idFields.map(({ id, value }, i) => (
							<TextField
								key={id}
								InputProps={{
									endAdornment:
										i !== 0 ? (
											<InputAdornment position="end">
												<IconButton
													color="primary"
													size="small"
													onClick={() => removeField(id)}
												>
													<RemoveCircleOutlineIcon fontSize="small" />
												</IconButton>
											</InputAdornment>
										) : (
											<></>
										),
								}}
								classes={{ root: classes.input }}
								fullWidth
								name={id}
								placeholder="Vložte uuid publikace"
								value={value}
								variant="outlined"
								onChange={handleChange}
							/>
						))}
						<Button
							className={classes.addButton}
							color="primary"
							startIcon={<AddCircleOutlineIcon />}
							onClick={addField}
						>
							Přidat publikaci
						</Button>
					</Grid>
					<Grid className={classes.submitButton} item xs={3}>
						<Button
							color="primary"
							disabled={disabledSubmitButton}
							type="submit"
							variant="contained"
						>
							Obohatit
						</Button>
					</Grid>
				</Grid>
			</form>
		</Paper>
	)
}
