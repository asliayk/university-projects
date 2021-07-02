from flask import Flask, render_template, request, redirect
from flask_mysqldb import MySQL
import yaml
import mysql.connector
import ctypes  

app = Flask(__name__)

# Configure db
db = yaml.load(open('db.yaml'))
app.config['MYSQL_HOST'] = db['mysql_host']
app.config['MYSQL_USER'] = db['mysql_user']
app.config['MYSQL_PASSWORD'] = db['mysql_password']
app.config['MYSQL_DB'] = db['mysql_db']

mysql = MySQL(app)


@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        userDetails = request.form
        cur = mysql.connection.cursor()
        if "lname" in userDetails:
            name = userDetails['lname']
            if("@" in name):
                cur.execute("SELECT Username FROM listeners WHERE Email='{}'".format(name))
            else:
                cur.execute("SELECT Username FROM listeners WHERE Username='{}'".format(name))
            myresult = cur.fetchall()
            if len(myresult)==0:
                cur.close()
                ctypes.windll.user32.MessageBoxW(0, "Wrong username or email. Please try again.", "Login Error", 0x1000)
                return render_template('index.html')
            cur.close()
            global uname
            uname = myresult[0][0]
            return redirect('/userpage')
        elif "arname" in userDetails:
            arname = userDetails['arname']
            arsurname = userDetails['arsurname']
            global aname
            aname = arname
            global asurname
            asurname = arsurname
            cur.execute("SELECT * FROM artists WHERE ArtistName='{}' AND ArtistSurname='{}'".format(arname, arsurname))
            myresult = cur.fetchall()
            if len(myresult)==0:
                cur.close()
                ctypes.windll.user32.MessageBoxW(0, "No artist with that name&surname. Please try again.", "Login Error", 0x1000)
                return render_template('index.html')
            cur.close()
            return redirect('/artistpage')    
    return render_template('index.html')

@app.route('/userpage')
def userpage():
    cur = mysql.connection.cursor()
    resultValue = cur.execute("SELECT * FROM listeners")
    userDetails = cur.fetchall()
    return render_template('userpage.html',userName=uname)

@app.route('/artistpage', methods=['GET', 'POST'])
def artistpage():
    cur = mysql.connection.cursor()
    if request.method == 'POST':
        form = request.form
        if "deletealbum" in form:
            albumID = form["deletealbum"]
            result = cur.execute("DELETE FROM albums WHERE AlbumID='{}'".format(albumID))
            mysql.connection.commit()
            return redirect('/artistpage') 
        elif "UAlbumID" not in form and "USongID" not in form and "SongID" not in form:
            albumID = form['AlbumID']
            title = form['AlbumTitle']
            genre = form['AlbumGenre']
            result = cur.execute("INSERT INTO albums (AlbumID, Genre, Title, ArtistName, ArtistSurname) VALUES ('{}','{}','{}','{}','{}')".format(albumID,genre,title,aname,asurname))
            mysql.connection.commit()
            return redirect('/artistpage') 
        elif "UAlbumGenre" in form:
            albumID = form['UAlbumID']
            genre = form['UAlbumGenre']
            result = cur.execute("UPDATE albums SET Genre = '{}' WHERE AlbumID='{}'".format(genre, albumID))
            mysql.connection.commit()
            return redirect('/artistpage') 
        elif "UAlbumTitle" in form:
            albumID = form['UAlbumID']
            title = form['UAlbumTitle']
            result = cur.execute("UPDATE albums SET Title = '{}' WHERE AlbumID='{}'".format(title, albumID))
            mysql.connection.commit()
            return redirect('/artistpage')   
        elif "SongID" in form:
            songID = form['SongID']
            result = cur.execute("INSERT IGNORE INTO contributions(SongID, ArtistName, ArtistSurname) VALUES ('{}', '{}', '{}')".format(songID, aname, asurname))
            mysql.connection.commit()
            return redirect('/artistpage')  
        elif "USongID" in form:
            songID = form['USongID']
            title = form['USongTitle']
            result = cur.execute("UPDATE songs SET Title= '{}' WHERE SongID='{}'".format(title, songID))
            mysql.connection.commit()
            return redirect('/artistpage')                  
    else:    
        resultValue = cur.execute("SELECT * FROM artists")
        userDetails = cur.fetchall()
        albums = cur.execute("SELECT * FROM albums WHERE ArtistName='{}' AND ArtistSurname='{}'".format(aname,asurname))
        albumsD = cur.fetchall()
        return render_template('artistpage.html',artistName=aname, artistSurname=asurname, albums=albumsD)    

@app.route('/artistpage/album', methods=['GET', 'POST'])
def artistAlbum():
    cur = mysql.connection.cursor()
    form = request.form
    albumID = ""
    albumTitle = ""
    if request.method == 'POST':
        if "delete" in form:
            songID = form['delete']
            albumID = form['albumid']
            deleted = cur.execute("DELETE FROM songs WHERE songID='{}'".format(songID))
            mysql.connection.commit()
        elif "songtitle" in form:
            songID = form['songID']
            albumID = form['albumid']
            title = form['songtitle']
            result = cur.execute("SELECT ArtistName, ArtistSurname FROM albums WHERE AlbumID='{}'".format(albumID))
            val = cur.fetchall()
            artistName = val[0][0]
            artistSurname = val[0][1]
            result = cur.execute("INSERT INTO songs(SongID, Title, AlbumID) VALUES ('{}','{}','{}')".format(songID, title, albumID))
            mysql.connection.commit()
            result2 = cur.execute("INSERT INTO contributions(SongID, ArtistName, ArtistSurname) VALUES ('{}','{}','{}')".format(songID, artistName, artistSurname))
            mysql.connection.commit()
        
        albumID = form["albumid"]
        result = cur.execute("SELECT Title FROM albums WHERE AlbumID='{}'".format(albumID))
        val = cur.fetchall()
        albumTitle = val[0][0]
        songs = cur.execute("SELECT * FROM songs WHERE AlbumID='{}'".format(albumID))
        songsDet = cur.fetchall()
        return render_template('artistAlbum.html', AlbumSongs = songsDet, albumID = albumID, albumName = albumTitle)

@app.route('/userpage/artists')
def artists():
    cur = mysql.connection.cursor()
    resultValue = cur.execute("SELECT * FROM artists")
    artistsDetails = cur.fetchall()
    return render_template('artists.html',artistsDetails=artistsDetails)

@app.route('/userpage/albums', methods=['GET', 'POST'])
def albums():
    cur = mysql.connection.cursor()
    if request.method == 'POST':
        form = request.form
        albumID = form['like']
        result2 = cur.execute("SELECT Username, AlbumID FROM likedalbums WHERE Username='{}' and AlbumID='{}'".format(uname, albumID))
        if result2!=0:
            ctypes.windll.user32.MessageBoxW(0, "You have already liked that album!", "Like Error", 0x1000) 
        else:
            result = cur.execute("INSERT INTO likedalbums(Username, AlbumID) VALUES('{}','{}')".format(uname, albumID))
            mysql.connection.commit()
    resultValue = cur.execute("SELECT * FROM albums")
    albumsDetails = cur.fetchall()
    return render_template('albums.html',albumsDetails=albumsDetails)   

@app.route('/userpage/songs', methods=['GET', 'POST'])
def songs():
    cur = mysql.connection.cursor()
    if request.method == 'POST':
        form = request.form
        songID = form['like']
        result = cur.execute("SELECT * FROM likedsongs WHERE Username='{}' AND SongID='{}'".format(uname, songID))
        if result==0:
            sqlFormula = "INSERT INTO likedsongs (Username, SongID) VALUES ('{}','{}')".format(uname, songID)
            cur.execute(sqlFormula)
            mysql.connection.commit()
        else:
            ctypes.windll.user32.MessageBoxW(0, "You have already liked that song!", "Like Error", 0x1000)    

    resultValue = cur.execute("SELECT * FROM songs")
    songsDetails = cur.fetchall()
    albumTitles = []
    totalLikes = []
    owners = []
    producers = []
    for song in songsDetails:
        temp = cur.execute("SELECT Title,ArtistName,ArtistSurname FROM albums WHERE AlbumID='{}'".format(song[2]))
        val = cur.fetchall()
        albumTitles.append(val[0][0])
        owners.append(val[0][1])
        owners.append(val[0][2]) 
        temp2 = cur.execute("SELECT * FROM likedsongs WHERE SongID='{}'".format(song[0]))
        totalLikes.append(temp2)
        temp3 = cur.execute("SELECT ArtistName,ArtistSurname FROM contributions WHERE SongID='{}'".format(song[0]))
        val2 = cur.fetchall()
        pString = ""
        for i, artist in enumerate(val2):
            pString+= artist[0] + " " + artist[1]
            if i+1!=temp3:
                pString+=", "
        producers.append(pString)
    return render_template('songs.html',songsDetails=songsDetails, enumerate=enumerate, titles = albumTitles, likes=totalLikes, owners=owners, producers=producers)

@app.route('/userpage/songs/popularsongs', methods=['GET', 'POST'])
def popularSongs():  
    cur = mysql.connection.cursor()
    if request.method == 'POST': 
        form = request.form
        name = form['artistname']
        nameList = name.split()
        result = cur.execute("SELECT * from likedsongs WHERE SongID IN (SELECT SongID FROM songs WHERE SongID IN (SELECT SongID from contributions WHERE ArtistName='{}' AND ArtistSurname='{}'))".format(nameList[0],nameList[1]))    
        songs = cur.fetchall()
        songList = []
        for song in songs:
            result = cur.execute("SELECT * from songs WHERE SongID='{}'".format(song[2]))
            songDet = cur.fetchall()
            if len(songList)==0:
                songList.append((songDet[0][0], songDet[0][1], songDet[0][2], 1))
            else:    
                add = True
                index = -1
                for i,songEl in enumerate(songList):
                   if songDet[0][1] == songEl[1]:
                     add=False
                     index=i
                     break   
                if add:
                   songList.append((songDet[0][0], songDet[0][1], songDet[0][2], 1))
                else:
                   tempEl = songList[index]
                   songList[index] = (tempEl[0],tempEl[1],tempEl[2],tempEl[3]+1)          
        songList = sorted(songList, key=lambda x:x[3], reverse=True)             
        return render_template("popularSongs.html", arname = name, songs = songList)

@app.route('/userpage/likedsongs', methods=['GET', 'POST'])
def likedSongs():
    cur = mysql.connection.cursor()
    if request.method == 'POST':
        form = request.form
        name = form['username']
    else:
        name = uname         
    resultValue = cur.execute("SELECT * FROM songs WHERE SongID IN (SELECT SongID FROM likedsongs WHERE Username='{}')".format(name))
    songsValue = cur.fetchall()
    if request.method == 'POST':
        return render_template('likedSongs.html',songsDetails=songsValue, name=name + "'s")
    else:
        return render_template('likedSongs.html',songsDetails=songsValue, name="your")    
         

@app.route('/userpage/songsofgenre', methods=['GET', 'POST'])
def songsOfGenre():
    if request.method == 'POST':
        form = request.form
        genre = form['genre']
        cur = mysql.connection.cursor()
        resultValue = cur.execute("SELECT * FROM songs WHERE AlbumID IN (SELECT AlbumID FROM albums WHERE Genre='{}')".format(genre))
        songsValue = cur.fetchall()
        return render_template('songsOfGenre.html',songsDetails=songsValue, genre=genre)        

@app.route('/userpage/songsofkeyword', methods=['GET', 'POST'])
def songsOfKeyword():
    if request.method == 'POST':
        form = request.form
        keyword = form['keyword']
        cur = mysql.connection.cursor()
        resultValue = cur.execute("SELECT * FROM songs WHERE Title LIKE '%{}%'".format(keyword))
        songsValue = cur.fetchall()
        return render_template('songsOfKeyword.html', songsDetails=songsValue, keyword=keyword)                            

@app.route('/userpage/albums/albumSongs', methods=['GET', 'POST'])
def albumSongs():
    if request.method == 'POST':
        form = request.form
        cur = mysql.connection.cursor()
        if 'like2songID' in form:
            songID = form['like2songID']
            albumID = form['like2albumID']
            albumName = form['like2albumName']
            result = cur.execute("SELECT * FROM likedsongs WHERE Username='{}' AND SongID='{}'".format(uname, songID))
            if result==0:
                sqlFormula = "INSERT INTO likedsongs (Username, SongID) VALUES ('{}','{}')".format(uname, songID)
                cur.execute(sqlFormula)
                mysql.connection.commit()
            else:
                ctypes.windll.user32.MessageBoxW(0, "You have already liked that song!", "Like Error", 0x1000)    
            resultValue = cur.execute("SELECT * FROM songs WHERE AlbumID='{}'".format(albumID))
            albumSongs = cur.fetchall()
            return render_template('albumSongs.html',albumID=albumID,albumName=albumName, albumSongs=albumSongs)
        else: 
            albumdet = form['albumdet']
            albumInfo = albumdet.split("-")
            resultValue = cur.execute("SELECT * FROM songs WHERE AlbumID='{}'".format(albumInfo[0]))
            albumSongs = cur.fetchall()
            return render_template('albumSongs.html',albumID=albumInfo[0],albumName=albumInfo[1], albumSongs=albumSongs)   

@app.route('/userpage/artists/artistSongs', methods=['GET', 'POST'])
def artistSongs():
   if request.method == 'POST':
        cur = mysql.connection.cursor()
        form = request.form
        if 'like' in form:
            songID = form['like']
            result = cur.execute("SELECT * FROM likedsongs WHERE Username='{}' AND SongID='{}'".format(uname, songID))
            if result==0:
                sqlFormula = "INSERT INTO likedsongs (Username, SongID) VALUES ('{}','{}')".format(uname, songID)
                cur.execute(sqlFormula)
                mysql.connection.commit()
            else:
                ctypes.windll.user32.MessageBoxW(0, "You have already liked that song!", "Like Error", 0x1000)    
 
        artistDetails = request.form
        name = form['artistname']
        nameList = name.split()
        resultValue = cur.execute("SELECT * FROM songs WHERE SongID IN (SELECT SongID from contributions WHERE ArtistName='{}' AND ArtistSurname='{}')".format(nameList[0],nameList[1]))
        artistSongs = cur.fetchall()
        return render_template('artistSongs.html', artistName = nameList[0]+ " " + nameList[1], artistSongs=artistSongs)  

@app.route('/userpage/artists/artistAlbums', methods=['GET', 'POST'])
def artistAlbums():
   if request.method == 'POST':
        artistDetails = request.form
        name = artistDetails['artistname']
        nameList = name.split()
        cur = mysql.connection.cursor()
        resultValue = cur.execute("SELECT * FROM albums WHERE ArtistName='{}' AND ArtistSurname='{}'".format(nameList[0],nameList[1]))
        artistAlbums = cur.fetchall()
        return render_template('artistAlbums.html', artistAlbums=artistAlbums)   

@app.route('/userpage/artists/contributors', methods=['GET', 'POST'])
def contributors():
   if request.method == 'POST':
        artistDetails = request.form
        name = artistDetails['artistname']
        nameList = name.split("-")
        cur = mysql.connection.cursor()
        resultValue = cur.execute("CALL getProducers('{}','{}')".format(nameList[0],nameList[1]))
        contributors = cur.fetchall()
        contList = list(contributors)
        contributorList = list(set([i for i in contList]))
        return render_template('contributors.html', artist=nameList, enumerate=enumerate, contributors=contributorList)           

@app.route('/userpage/artists/popularArtists', methods=['GET', 'POST'])
def popularArtists():
   if request.method == 'POST':
        cur = mysql.connection.cursor()
        resultValue = cur.execute("SELECT * FROM artists")
        artists = cur.fetchall()
        lists = []
        for artist in artists:
            tlist = list(artist)
            result = cur.execute("SELECT * FROM likedsongs WHERE SongID IN(SELECT SongID FROM contributions WHERE ArtistName = '{}' AND ArtistSurname = '{}')".format(tlist[0], tlist[1]))
            songs = cur.fetchall()
            num = len(songs)
            tlist.append(num)
            lists.append(tlist)
        artistList = sorted(lists, key=lambda x:x[2], reverse=True)     
        return render_template('popularArtists.html', artists=artistList)            
                    

if __name__ == '__main__':
    app.run() 
