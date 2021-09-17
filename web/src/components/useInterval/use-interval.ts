import {useEffect, useRef} from 'react'

type Callback = () => Promise<void>

export const useInterval = (callback: Callback, delay: number) => {

  const savedCallback = useRef<Callback | null>(null)

  useEffect(( ) => {
    savedCallback.current = callback;

    return () =>{
      savedCallback.current = null
    }
  }, [callback])


  useEffect(() => {
    function tick() {
      if(savedCallback.current != null) {
        savedCallback.current();
      }
    }

    if (delay !== null) {
      let id = setInterval(tick, delay);
      return () => clearInterval(id);
    }

  }, [delay]);
}