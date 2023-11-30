from pprint import pprint
import urllib.request
import time

def get_job_list(keywords, zip_code):
    try:
        # Creating a session object to handle cookies
        # session = requests.Session()

        # Headers to mimic a web browser
        headers = {
            'authority': 'www.indeed.com',
            'accept': '*/*',
            'accept-language': 'en-US,en;q=0.9',
            # 'cookie': 'PP=1; CTK=1hfuma1r42bmj000; INDEED_CSRF_TOKEN=MOAPcHNRyUP2KBR3tsySrMV4iZUKb3JH; LV="LA=1700763338:CV=1700763338:TS=1700763338"; __cf_bm=bQKBskC5Ks7sayvxTx_lq1bbRINWsd9qvThbvT15zJg-1700763338-0-ATILtFNunjgdH9FXQUEk7lm7JRmPK37s0Sh9bTxtEe63quo6rl7w25jxuOHhM9FgZSWBduv7xopLxnWL6CEE5ug=; _cfuvid=diHeMkYKGqfWSwjaekr.yjJeb6PnhzbVdDFWlGVW5G0-1700763338622-0-604800000; CSRF=aGE8XiIQhF1gNXiZGP3DQ7OxaXbIPPOo; hpnode=1; SURF=P9HWBDOOZvBtJOH5YnofNYUpSLndWDrO; _ga=GA1.2.1719644816.1700763339; _gid=GA1.2.963695430.1700763339; _gat=1; _gcl_au=1.1.1890197115.1700763364; indeed_rcc="LV:CTK"; SHARED_INDEED_CSRF_TOKEN=MOAPcHNRyUP2KBR3tsySrMV4iZUKb3JH; LC="co=US"; RQ="q=software+engineer&l=&ts=1700763392019"; JSESSIONID=55C69D22EA88DBA0958353EEE410D8EC',
            'referer': 'https://www.indeed.com/jobs?q=software+engineer',
            'sec-ch-ua': '"Google Chrome";v="119", "Chromium";v="119", "Not?A_Brand";v="24"',
            'sec-ch-ua-mobile': '?0',
            'sec-ch-ua-platform': '"macOS"',
            'sec-fetch-dest': 'empty',
            'sec-fetch-mode': 'cors',
            'sec-fetch-site': 'same-origin',
            'user-agent': 'Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36',
        }

        query = "+".join(keywords.split())
        url = f"https://www.indeed.com/jobs?q={query}&l={zip_code}&radius=5"
        print("URL: ", url)

        # Simulate human reading time
        time.sleep(1)

        # Making the request with custom headers
        request = urllib.request.Request(url, headers=headers)
        response = urllib.request.urlopen(request).read()
        if response.status_code == 200:
            return (response.decode('utf-8'))
        else:
            return f"Error fetching data: HTTP Status Code {response.status_code}"
    except Exception as e:
        return str(e)

# Example usage
html_content = get_job_list("software engineer", "60616")
pprint(html_content)
