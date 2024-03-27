import { ErrorMessage } from 'formik'
import { FormGroup, FormFeedback, Input } from 'reactstrap'

function FormControl({ field, form, placeholder, type = 'text', label, id, innerRef }) {
    const { errors, touched } = form
    const { name, value, onBlur, onChange } = field
    const error = errors[name] && touched[name]
    const handleChange = (e) => {
        if (type == 'file') {
            const file = {
                target: {
                    value: e.target.files[0],
                    name: name
                }
            }
            onChange(file)
        } else {
            onChange(e)
        }
    }
    return (
        <FormGroup>
            <label htmlFor={id} className='form-label h6'>{label}</label>
            <input className='form-control' type={type} placeholder={placeholder}
                name={name} id={id}
                value={(type === 'file' ? undefined : value)}
                onChange={handleChange} onBlur={onBlur}
                invalid={error ? 'true' : 'false'} autoComplete="off" ref={innerRef} />
            {error && <p style={{ color: 'red' }}>{errors[name]}</p>}
        </FormGroup>
    )
}
export default FormControl